package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.geosvr.dtn.systemlib.util.IpHelper;


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
		// TODO Auto-generated method stub
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

}
