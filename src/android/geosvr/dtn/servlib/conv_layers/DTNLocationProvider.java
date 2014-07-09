package android.geosvr.dtn.servlib.conv_layers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import android.geosvr.dtn.servlib.discovery.Location;
import android.geosvr.dtn.systemlib.util.ByteHelper;

/*
 * 与AODV的位置交互接口
 * 经纬度是xxx.xxxxxx
 * 可以用四字节表示xxxxxxxxx
 * accuracy表示精确到小数点后几位（最多6位）
 * 该交互接口在Location：init中启动。
 */
public class DTNLocationProvider implements Runnable{
	public static final int accuracy = 6;
	
	private Thread thread_;
	
	public void start(){
		thread_ = new Thread(this);
		thread_.start();
	}
	
	@Override
	public void run() {
		byte[] buf = new byte[16];//
		DatagramPacket recvPacket = new DatagramPacket(buf,
				buf.length);
		DatagramPacket sendPacket = null;
		DatagramSocket sock_loc_req = null;
		DatagramSocket sock_loc_reply = null;
		try {
			sock_loc_req = new DatagramSocket(Netw_layerInteractor.DTN_LOCATION_PORT);
			sock_loc_reply = new DatagramSocket(Netw_layerInteractor.AODV_LOCATION_PORT);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				sock_loc_req.receive(recvPacket);
				double longitude = Location.getInstance().getLongitude();
				double latitude  = Location.getInstance().getLatitude();
				int x = (int)(longitude * Math.pow(10, accuracy));
				int y = (int)(latitude * Math.pow(10, accuracy));
				String host = "127.0.0.1";
				byte[] xb = ByteHelper.int_to_byte_array(x);
				byte[] yb = ByteHelper.int_to_byte_array(y);
				for(int i = 0; i < 4; i++){
					buf[i+8] = xb[i];
					buf[i+12] = yb[i];
				}
				DatagramPacket packet = new DatagramPacket(buf, buf.length,
						InetAddress.getByName(host),Netw_layerInteractor.AODV_LOCATION_PORT);
				sock_loc_reply.send(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
