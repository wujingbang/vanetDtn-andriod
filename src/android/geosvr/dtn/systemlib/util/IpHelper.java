package android.geosvr.dtn.systemlib.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

public class IpHelper {

    private static int byte2int(byte b) {
		if(b>=0){
			return b;
		}else{
			return b+256;
		}
	}
	/**
	 * ipbyte2ipstr
	 * byte[]转字符串ip
	 */
    public static String ipbyte2ipstr(byte[] ip) {
    	String ipStr="";
    	for(int i=0;i<4;i++){
    		ipStr+=Integer.toString(byte2int(ip[i]));
    		if(i < 3){
    			ipStr+=".";
    		}
    	}
    	return ipStr;
    }
    
    /**
     * 这种方法会把所有的IP地址查出来，再根据接口挑选。
     */
    public static InetAddress getLocalIpAddress() {
         try {
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                 NetworkInterface intf = en.nextElement();
                 for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                         enumIpAddr.nextElement();
                         if (intf.getName().equals("adhoc0"))
                         {
                                 //取第二个值
                                 InetAddress inetAddress = enumIpAddr.nextElement();
                                 if (!inetAddress.isLoopbackAddress()) {
                                 return inetAddress;
                             }
                         } 
                 }
             }
         } catch (SocketException ex) {
             Log.e("testAndroid1", ex.toString());
         }
         return null;
    } 
    
    /**
     * String IP 转 EndpointID String
     * dtn://192.168.1.3.wu.com
     */
    public static String ipstr2Idstr(String ipstr) {
    	String str = new String("dtn://");
    	str += ipstr;
    	str += ".wu.com";
    	return str;
    }

}
