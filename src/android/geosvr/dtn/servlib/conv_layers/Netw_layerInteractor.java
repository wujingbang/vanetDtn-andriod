package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.PriorityBlockingQueue;


import android.geosvr.dtn.DTNManager;
import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundleList;
import android.geosvr.dtn.servlib.bundling.ForwardingInfo;
import android.geosvr.dtn.servlib.bundling.event.BundleEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkStateChangeRequest;
import android.geosvr.dtn.servlib.bundling.event.event_type_t;
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
	
	public static final int DTNREGISTERPORT=9999;
	public static final int DTNPORT=10000;
	public static final int DTN_FIND_NEIGHBOUR_PORT = 10001;
	public static final int DTN_RECV_NEIGHBOUR_PORT = 10002;
	public static final int DTN_LOCATION_PORT = 10003;
	public static final int AODV_LOCATION_PORT = 10004;
	
	//RERR =126;RCVP = 122;
	public static enum infotype_from_lowlayer {
		RCVP            (122),
		RRER         	(126);
		
		private int type_;
		private infotype_from_lowlayer(int type){
			type_ = type;
		};
		
	    public static infotype_from_lowlayer valueOf(int value) {
	        switch (value) {
	        case 122:
	            return RCVP;
	        case 126:
	            return RRER;
	        default:
	            return null;
	        }
	    }
	}
	
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
	
	private boolean check_dup(byte[] b1, byte[] b2){
		for(int i = 0; i < b1.length; i++) {
			if (b1[i] == b2[i]) continue;
			else return false;
		}
		return true;//duplication
	}
	
	@Override
	public void run() {
		register2netw_layer();//注册
		byte[] check_b = new byte[16];
		byte[] recvBuf = new byte[16];
		byte[] srcip = new byte[5];
		byte[] dstip = new byte[5];
		byte[] avail = new byte[5];
		while (true) {
			try {
				DatagramPacket recvPacket = new DatagramPacket(recvBuf,
						recvBuf.length);
				sock_interact_.receive(recvPacket);
				//此处要加入防止重复信息的机制
				if (check_dup(recvBuf, check_b))
					continue;

				for (int k = 0; k < 4; k++) {
					srcip[k] = recvBuf[k];
					dstip[k] = recvBuf[k + 4];
					avail[k] = recvBuf[k + 8];
				}
				byte type_b = recvBuf[12];
				infotype_from_lowlayer type = infotype_from_lowlayer.valueOf(type_b);
				
				String srcipStr = IpHelper.ipbyte2ipstr(srcip);
				String dstipStr = IpHelper.ipbyte2ipstr(dstip);
				String lastAvailStr = IpHelper.ipbyte2ipstr(avail);
				String srcId = IpHelper.ipstr2Idstr(srcipStr);
				String dstId = IpHelper.ipstr2Idstr(dstipStr);
				String lastAvailId = IpHelper.ipstr2Idstr(lastAvailStr);
				
				
				//获得本地ip
				InetAddress local_addr = IpHelper.getLocalIpAddress();
				String localIp = local_addr.toString().substring(1);
				
				//菜：Debug info
//				System.out.println("-------------------------------");
//				System.out.println("-------------------------------");
//				System.out.println("local:"+localIp);
//				System.out.println("dst:"+dstId+", lastAvail:"+lastAvailId+", src:"+srcId);
//				System.out.println("type:"+type);
//				
				//RERR =126;RCVP = 122;
				if(type == null) {
					Log.e(TAG, "unknow type!!");
					continue;
				}
				switch(type){
				case RRER:
					Log.e(TAG, "************RECV RRER**************");
					//判断本节点是否是离断开链路最近的DTN节点以及是否是此段链路的第一个DTN节点
					if(localIp.equals(lastAvailStr))//第一个DTN节点
					{
						
						//关闭dstID对应的直接连接，无需其他操作
						shutdownDirectLink(dstId, type);
						System.out.println("This is the first DTN node:"+localIp+"shutdown link");
					}
					else if(localIp.equals(srcipStr))
					{
						System.out.println("This is the SRC DTN node:"+localIp+"shutdown link");
						//关闭dstID对应的直接连接。
						shutdownDirectLink(dstId, type);
						
//						flash_pendingBundleState(dstId);
						
						add_link(lastAvailStr, lastAvailId);
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							// TODO: handle exception
						}
						add_routeEntry(dstId, lastAvailId);//必须放最后！我把add_route对外开放了，实际上可以作为事件！
					}
					//若均不是，不必进行操作，直接丢弃
					break;
				case RCVP:
					Log.e(TAG, "************RECV RCVP**************");
					//留出遍历发送队列，并根据bundle包的目的地址发起连接的接口，实验中首先按照接收到rcvp包恢复通路
					//判断本节点是否是离断开链路最近的DTN节点以及是否是此段链路的第一个DTN节点
					if(localIp.equals(lastAvailStr))//第一个DTN节点
					{
						//开启dstID对应的直接连接，无需其他操作
						add_link(dstipStr,dstId);
						System.out.println("This is the first DTN node:"+localIp+" add link");
					}
					else if(localIp.equals(srcipStr))
					{
						System.out.println("This is the SRC DTN node:"+localIp+" add link");
						//关闭dstID对应的直接连接。
						
						
						
						shutdownDirectLink(lastAvailId, type);
//						flash_pendingBundleState(dstId);
						add_link(dstipStr,dstId);
						
						
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							
						}
						//add_routeEntry(dstId, lastAvailId);//必须放最后！我把add_route对外开放了，实际上可以作为事件！
					}
					//若均不是，不必进行操作，直接丢弃
					break;
					default:
						Log.e(TAG,"Invalid Message from AODV,discarded!");
						break;
				}
				
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
	private void shutdownDirectLink(String dstId, infotype_from_lowlayer type) {
		BundleDaemon Daemon = BundleDaemon.getInstance();
		
/*		PriorityBlockingQueue<BundleEvent> eventq = Daemon.test_get_eventq();
		Iterator<BundleEvent> iter = eventq.iterator();
		while (iter.hasNext()) {
			if (iter.next().type() == event_type_t.BUNDLE_TRANSMITTED) {
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
				iter = eventq.iterator();
			}
		}
*/		
		
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
//				route.link().inflight().contains()
//				if (type == infotype_from_lowlayer.RRER) {
					flash_queueBundleState(route.link().queue());
					route.link().queue().clear();
					flash_queueBundleState(route.link().inflight());
					route.link().inflight().clear();
//				} else if (type == infotype_from_lowlayer.RCVP) {
//					flash_queueWithoutInflight(route.link().queue(), route.link().inflight());
//				}
				
				route.link().set_state(state_t.UNAVAILABLE);//这里还可以用link().close()，可以试一试
//				route.link().close();//close没有对于CLMSG_BREAK_CONTACT的处理,导致断不了。
				route.link().get_lock().unlock();
			}
		}
	}
	
	/**
	 * 在通路包的处理中，需要中断发送的link中已经有包被成功传送并且产生了transmit事件
	 * 接着在之前的处理中直接把这些包一并删除
	 * 导致BundleDeamon中对于transmit事件的处理出现空指针错误。
	 * @param bundleList
	 * @param inflightList
	 */
	private void flash_queueWithoutInflight(BundleList bundleList, BundleList inflightList) {
		bundleList.get_lock().lock();
		try {
			ListIterator<Bundle> iter = bundleList.begin();
			while (iter.hasNext()) {
				Bundle bundle = iter.next();
				if (inflightList.contains(bundle))
					continue;
				else {
					int count = bundle.fwdlog().get_count(
							ForwardingInfo.state_t.QUEUED.getCode(),
							1);
					if (count > 0) {
						bundle.fwdlog().clear();
					}
				
					//在list中删除单个bundle
					bundleList.erase(bundle, false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bundleList.get_lock().unlock();
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
		
		/**************此处导致了死线程。需要以后处理！**********************/
///		if (link == null || link.state() == state_t.UNAVAILABLE) {
			//在这里post了link_created事件，此处写死了tcp
			link = null;
			link = cm.new_opportunistic_link(ConvergenceLayer.find_clayer("tcp"), ipcombostr, lastEndid);

		/**************此处导致了死线程。需要以后处理！**********************/	
///			if (link == null) {
				//log.d(TAG, "failed to create opportunistic link");
///				return;
///			}
			link.lock().lock();
			
			link.set_state(Link.state_t.AVAILABLE);
			link.open();
			link.lock().unlock();
///		}
///		else {
///			assert (link != null);
///			if (!link.isNotUnavailable()) {
///				link.lock().lock();
///				link.set_nexthop(ipcombostr);
//				link.set_state(Link.state_t.AVAILABLE);
///				link.lock().unlock();
///			
///			}
			
///		}
		cm.get_lock().unlock();
	}
	
//	public 

}
