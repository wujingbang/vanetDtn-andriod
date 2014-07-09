package android.geosvr.dtn.servlib.discovery;

import java.net.InetAddress;

public class PASVDiscoveryHeader {
	/**
	 * Constructor
	 */
	public PASVDiscoveryHeader() {
		
		inet_addr = null;
		name_len = 0;
		sender_name = "";
		extraInfo = new PASVExtraInfo();		
	}
	

        private String inet_addr;                 // IPv4 address of CL
        private short name_len;                        // length of EID
        private String sender_name;                    // DTN URI of beacon sender
        private PASVExtraInfo extraInfo;
        
		public PASVExtraInfo getExtraInfo() {
			return extraInfo;
		}
		public String getInet_addr() {
			return inet_addr;
		}
		public void setInet_addr(String inet_addr) {
			this.inet_addr = inet_addr;
		}
		public short getName_len() {
			return name_len;
		}
		public void setName_len(short name_len) {
			this.name_len = name_len;
		}
		public String getSender_name() {
			return sender_name;
		}
		public void setSender_name(String sender_name) {
			this.sender_name = sender_name;
		}
}
