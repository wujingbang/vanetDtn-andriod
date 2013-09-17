package android.geosvr.dtn.servlib.routing.epidemic;

import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundleProtocol.status_report_reason_t;
import android.geosvr.dtn.servlib.bundling.event.BundleDeleteRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleDeliveredEvent;
import android.geosvr.dtn.servlib.naming.EndpointIDPattern;
import android.geosvr.dtn.servlib.reg.Registration;
import android.util.Log;

public class EpidemicRegistration extends Registration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "EpidemicRegistration";
	private EpidemicBundleRouter router_;

	public EpidemicRegistration(EpidemicBundleRouter router_) {
		super(PROPHET_REGID, new EndpointIDPattern(router_.localEid()
				+ "/prophet"), Registration.failure_action_t.DEFER, 0, 0, "");
		this.router_ = router_;
		set_active(true);
	}

	@Override
	public void deliver_bundle(Bundle bundle) {
		Log.d(TAG, "Epidemic bundle from " + bundle.source());
		router_.deliver_bundle(bundle);
		BundleDaemon.getInstance().post_at_head(new BundleDeliveredEvent(bundle, this));
		BundleDaemon.getInstance().post_at_head(new BundleDeleteRequest(bundle, status_report_reason_t.REASON_NO_ADDTL_INFO));
	}

}
