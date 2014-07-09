package android.geosvr.dtn.servlib.discovery;

import java.net.InetAddress;

public class PASVExtraInfo {
	private double longitude;
	private double latitude;
	private String nexthop; //ip_addr 

	public PASVExtraInfo(){}
	public PASVExtraInfo(double longitude,double latitude,String nexthop){
		this.longitude = longitude;
		this.latitude = latitude;
		this.nexthop = nexthop;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getNexthop() {
		return nexthop;
	}

	public void setNexthop(String nexthop) {
		this.nexthop = nexthop;
	}

}
