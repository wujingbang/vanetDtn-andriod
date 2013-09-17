package android.geosvr.dtn.applications;

import java.nio.ByteBuffer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.geosvr.dtn.DTNService;
import android.geosvr.dtn.applib.DTNAPIBinder;
import android.geosvr.dtn.applib.DTNAPICode.dtn_api_status_report_code;
import android.geosvr.dtn.applib.DTNAPICode.dtn_bundle_payload_location_t;
import android.geosvr.dtn.applib.DTNAPICode.dtn_bundle_priority_t;
import android.geosvr.dtn.applib.types.DTNBundleID;
import android.geosvr.dtn.applib.types.DTNBundlePayload;
import android.geosvr.dtn.applib.types.DTNBundleSpec;
import android.geosvr.dtn.applib.types.DTNEndpointID;
import android.geosvr.dtn.applib.types.DTNHandle;
import android.geosvr.dtn.servlib.bundling.BlockInfo;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.systemlib.util.BufferHelper;
import android.geosvr.dtn.systemlib.util.IByteBuffer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ProcessPayload extends Service {

	/**
	 * Default expiration time in seconds, set to 30days *100?TODO
	 */
	private static final int EXPIRATION_TIME = 3600*24*30*100;
	
	/**
	 * Set delivery options to don't flag at all
	 */
	private static final int DELIVERY_OPTIONS = 0;
	
	/**
	 * Set priority to normal sending
	 */
	private static final dtn_bundle_priority_t PRIORITY = dtn_bundle_priority_t.COS_NORMAL;

	protected static final String TAG = "PayloadReceiver";
	
	protected DTNAPIBinder dtn_api_binder_;

	private ServiceConnection conn_;
	
	private String payload;
	
	private String appBlockData;

	private String gateway;
	
	
	public void onCreate(Bundle params) { 
	    super.onCreate(); 
	}
	
	public void onStart(Intent intent, int startId) {
	    Log.d("PayloadReceiver", "service started..");

		bindDTNService();
		
		gateway = "dtn://city.bytewalla.com/";//TODO
		
		payload = intent.getStringExtra("payload");
		int serviceType = intent.getIntExtra("serviceType", 0);//TODO
		String appSource = BundleDaemon.getInstance().local_eid().toString();
		String appDest = "//city.bytewalla.com/";//TODO
		appBlockData = ""+((char)1)+""+((char)0)+appSource+((char)0)+appDest;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	} 
	

	
	private void failed() {
		Log.e("PayloadReceiver", "Service not running");
		//TODO: use startactivity and give -1 as result..
		return;
	}

	/**
	 * bind the DTNService to use the API later
	 */
	private void bindDTNService()
	{
		
		
		conn_ = new ServiceConnection() {

			

			
			public void onServiceConnected(ComponentName arg0, IBinder ibinder) {
				Log.i(TAG, "DTN Service is bound");
				dtn_api_binder_ = (DTNAPIBinder) ibinder;
				
				//Log.v("PayloadReceiver", "payload2: "+payload);
				
				byte[] message_byte_array = payload.getBytes();
				//IByteBuffer appBlockData = IByteBuffer.
				//appBlockData.put(apbStr.getBytes());
				
				String dest_eid = gateway;
				//TODO: set destination eid in the configuration file
				
				// Setting DTNBundle Payload according to the values
				DTNBundlePayload dtn_payload = new DTNBundlePayload(dtn_bundle_payload_location_t.DTN_PAYLOAD_MEM);
				dtn_payload.set_buf(message_byte_array);
				
			//	DTNBundlePayload dtn_payload = new DTNBundlePayload(dtn_bundle_payload_location_t.DTN_PAYLOAD_FILE);
				//dtn_payload.set_file(new File("/sdcard/test3MB.zip"));
			//	dtn_payload.set_file(new File("/sdcard/test.htm"));
				   
				// Start the DTN Communication
				DTNHandle dtn_handle = new DTNHandle();
				dtn_api_status_report_code open_status = dtn_api_binder_.dtn_open(dtn_handle);
				
				if (open_status != dtn_api_status_report_code.DTN_SUCCESS) {
					failed();
				}
				
				try {
					DTNBundleSpec spec = new DTNBundleSpec();
					
					// set destination from the user input
					spec.set_dest(new DTNEndpointID(dest_eid));
					
					// set the source EID from the bundle Daemon
					spec.set_source(new DTNEndpointID(BundleDaemon.getInstance().local_eid().toString()));
						
					// Set expiration in seconds, default to 1 hour
					spec.set_expiration(EXPIRATION_TIME);
					// no option processing for now
					spec.set_dopts(DELIVERY_OPTIONS);
					// Set prority
					spec.set_priority(PRIORITY);
					
					// Data structure to get result from the IBinder
					DTNBundleID dtn_bundle_id = new DTNBundleID();
					
					dtn_api_status_report_code api_send_result =  dtn_api_binder_.dtn_send(dtn_handle, 
							spec, 
							dtn_payload, 
							dtn_bundle_id,
							appBlockData//,
							//apblen
							);
					
					// If the API fail to execute throw the exception so user interface can catch and notify users
					if (api_send_result != dtn_api_status_report_code.DTN_SUCCESS) {
						failed();
					}
				}
				finally {
					dtn_api_binder_.dtn_close(dtn_handle);
				}
			}


			
			public void onServiceDisconnected(ComponentName arg0) {
				Log.i(TAG, "DTN Service is Unbound");
				dtn_api_binder_ = null;
			}

			};

			Intent i = new Intent(this, DTNService.class);
			bindService(i, conn_, Context.BIND_AUTO_CREATE);	
	}

}