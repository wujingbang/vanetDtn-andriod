package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.ListIterator;


import android.geosvr.dtn.DTNManager;
import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundleList;
import android.geosvr.dtn.servlib.bundling.ForwardingInfo;
import android.geosvr.dtn.servlib.bundling.event.BundleEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkStateChangeRequest;
import android.geosvr.dtn.servlib.bundling.exception.BundleListLockNotHoldByCurrentThread;
import android.geosvr.dtn.servlib.contacts.ContactManager;
import android.geosvr.dtn.servlib.contacts.Link;
import android.geosvr.dtn.servlib.contacts.Link.state_t;
import android.geosvr.dtn.servlib.discovery.IPDiscovery;
import android.geosvr.dtn.servlib.discovery.IPDiscovery.cl_type_t;
import android.geosvr.dtn.servlib.naming.EndpointID;
import android.geosvr.dtn.servlib.naming.EndpointIDPattern;
import android.geosvr.dtn.servlib.routing.RouteEntry;
import android.geosvr.dtn.servlib.routing.RouteEntryVec;
import android.geosvr.dtn.systemlib.thread.MsgBlockingQueue;
import android.geosvr.dtn.systemlib.util.IpHelper;
import android.util.Log;



public class Netw_layerInteractor implements Runnable {

	
	private final int DTNREGISTERPORT=9999;
	private final int DTNPORT=10000;
	
	private DatagramSocket sock_interact_;
	
	private Thread thread_;
	private static Netw_layerInteractor instance_ = null;
	private static final String TAG = "Netw_layerInteractor";

	private void register2netw_layer() {
		DatagramSocket sock_register = null;
		try {
			sock_register = new DatagramSocket(DTNREGISTERPORT);
			String s = "DTN register!";
			String host = "127.0.0.1";
			byte[] data = s.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length,
					InetAddress.getByName(host),DTNREGISTERPORT);
			sock_register.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sock_register.close();
		}
		
	}
	
	public void init() {
		start();
	}
	
	/**
	 * private Constructor
	 */
	private Netw_layerInteractor() {
		try {
			sock_interact_ = new DatagramSocket(DTNPORT);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	

	/**
	 * 单例
	 */
	public static Netw_layerInteractor getInstace() {
		if (instance_ == null)
			instance_ = new Netw_layerInteractor();
		return instance_;
	}

	/**
	 * 线程开启
	 */
	private void start() {
		thread_ = new Thread(this);
		thread_.start();

	}
	

	@Override
	public void run() {
		register2netw_layer();//注册
		while (true) {
			try {
				byte[] recvBuf = new byte[30];
				DatagramPacket recvPacket = new DatagramPacket(recvBuf,
						recvBuf.length);
				sock_interact_.receive(recvPacket);

				byte[] dstip = new byte[5];
				byte[] avail = new byte[5];
				for (int k = 0; k < 4; k++) {
					dstip[k] = recvBuf[k];
					avail[k] = recvBuf[k + 4];
				}

				String dstipStr = IpHelper.ipbyte2ipstr(dstip);
				String lastAvailStr = IpHelper.ipbyte2ipstr(avail);
				String dstId = IpHelper.ipstr2Idstr(dstipStr);
				String lastAvailId = IpHelper.ipstr2Idstr(lastAvailStr);
				
				//关闭dstID对应的直接连接。
				shutdownDirectLink(dstId);
				
//				flash_pendingBundleState(dstId);
				
				add_link(lastAvailStr, lastAvailId);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				add_routeEntry(dstId, lastAvailId);//必须放最后！我把add_route对外开放了，实际上可以作为事件！

				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * 加入一条新的路由项
	 */
	private void add_routeEntry(String dstId, String lastAvailId) {
		BundleDaemon Daemon = BundleDaemon.getInstance();
		
		RouteEntry route = new RouteEntry(new EndpointIDPattern(dstId+"/*"), 
										new EndpointIDPattern(lastAvailId+"/*"));
		route.set_action(ForwardingInfo.action_t.FORWARD_ACTION);
		Daemon.router().add_route(route);
	}
	/**
	 * 收到断路信息，关闭对应直接链接
	 */
	private void shutdownDirectLink(String dstId) {
		BundleDaemon Daemon = BundleDaemon.getInstance();
		RouteEntryVec matches = new RouteEntryVec();
		dstId += "/*";
		Link null_link = null;
		Daemon.router().getRoute_table()
			.get_matching(new EndpointID(dstId), null_link, matches);
		
		Iterator<RouteEntry> itr = matches.iterator();
		while (itr.hasNext()) {
			RouteEntry route = itr.next();
			if (route.IsDirectLink()){
				route.link().get_lock().lock();
				//清空link队列
				flash_queueBundleState(route.link().queue());
				route.link().queue().clear();
				flash_queueBundleState(route.link().inflight());
				route.link().inflight().clear();
				
				route.link().set_state(state_t.UNAVAILABLE);//这里还可以用link().close()，可以试一试
//				route.link().close();
				route.link().get_lock().unlock();
			}
		}
	}
	
	/**
	 * 清空bundleList中断路对应link队列的queue状态
	 */
	private void flash_queueBundleState(BundleList bundleList) {
		bundleList.get_lock().lock();
		try {
			ListIterator<Bundle> iter = bundleList.begin();
			while (iter.hasNext()) {
				Bundle bundle = iter.next();
				int count = bundle.fwdlog().get_count(
						ForwardingInfo.state_t.QUEUED.getCode(),
						1);
				if (count > 0) {
					bundle.fwdlog().clear();
				}
			}

		} catch (BundleListLockNotHoldByCurrentThread e) {
			Log.e(TAG, "TableBasedRouter: reroute_all_bundles " + e.toString());
		} finally {
			bundleList.get_lock().unlock();
		}
	}
	/**
	 * 清空pending_list中断路对应link队列的queue状态
	 */
	private void flash_pendingBundleState(String dstId) {
		BundleList pending = BundleDaemon.getInstance().pending_bundles();
		
		pending.get_lock().lock();
		try {
			ListIterator<Bundle> iter = pending.begin();
			while (iter.hasNext()) {
				Bundle bundle = iter.next();
				int count = bundle.fwdlog().get_count(
						new EndpointIDPattern(dstId), 
						ForwardingInfo.state_t.QUEUED.getCode(),
						1);
				if (count > 0) {
					bundle.fwdlog().clear();
				}
			}

		} catch (BundleListLockNotHoldByCurrentThread e) {
			Log.e(TAG, "TableBasedRouter: reroute_all_bundles " + e.toString());
		} finally {
			pending.get_lock().unlock();
		}
	}

	/**
	 * 收到断路信息，加入到last avail的link
	 */
	private void add_link(String lastAvailIpStr, String lastAvailId) {
		
		String ipcombostr = "/"+lastAvailIpStr+":"+"4556";
		EndpointID lastEndid = new EndpointID(lastAvailId);
		
		BundleDaemon Daemon = BundleDaemon.getInstance();
		
		ContactManager cm = Daemon.contactmgr();
		cm.get_lock().lock();
		Link link = cm.find_link_to(lastEndid);
		
		if (link == null) {
			//在这里post了link_created事件，此处写死了tcp
			link = cm.new_opportunistic_link(
					(link==null) ? ConvergenceLayer.find_clayer("tcp") : link.clayer(), 
							ipcombostr, lastEndid);
			
			if (link == null) {
				//log.d(TAG, "failed to create opportunistic link");
				return;
			}
			link.lock().lock();
			
			link.set_state(Link.state_t.AVAILABLE);
			link.open();
			link.lock().unlock();
			// request to set link available
			//(event, notifier, timeout, at_back)
//			BundleEvent event = new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
//					ContactEvent.reason_t.USER);
//			Daemon.post_and_wait(event,//event.processed_notifier(),
//					new MsgBlockingQueue<Integer>(5),
//					-1/*5s*/, false);
//			Daemon.post_at_head(event);
//			Daemon.post(new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
//					ContactEvent.reason_t.USER));
		}
		else {
			assert (link != null);
			if (!link.isNotUnavailable()) {
				link.lock().lock();
				link.set_nexthop(ipcombostr);
//				link.set_state(Link.state_t.AVAILABLE);
				link.lock().unlock();

				// request to set link available
//				Daemon.post(new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
//						ContactEvent.reason_t.USER));
/*				BundleEvent event = new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
						ContactEvent.reason_t.USER);
//				Daemon.post_and_wait(event,//event.processed_notifier(),
//						new MsgBlockingQueue<Integer>(5),
//						-15s, false);
				Daemon.post_at_head(event);*/
			}
		}
		cm.get_lock().unlock();

	}

}
