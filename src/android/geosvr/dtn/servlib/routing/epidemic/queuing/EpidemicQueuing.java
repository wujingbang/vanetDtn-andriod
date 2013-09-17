package android.geosvr.dtn.servlib.routing.epidemic.queuing;

import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.config.DTNConfiguration;
import android.geosvr.dtn.servlib.routing.BundleRouter.Config;
import android.geosvr.dtn.servlib.storage.BundleStore;
import android.geosvr.dtn.servlib.storage.GlobalStorage;
import android.util.Log;

public abstract class EpidemicQueuing {

	/**
	 * Bundle Store
	 */
	private BundleStore bundleStore = BundleStore.getInstance();

	/**
	 * TAG for Android Logging
	 */
	private static String TAG = "Queuing";

	/**
	 * Singleton instance Implementation of the BundleStore
	 */
	private static EpidemicQueuing instance_ = null;

	/**
	 * Queuing Type
	 */
	private static String policy = "Fifo";

	/**
	 * @return the policy
	 */
	public static String getPolicy() {
		return policy;
	}

	/**
	 * @param policy
	 *            the policy to set
	 */
	public static void setPolicy(String policy) {
		EpidemicQueuing.policy = policy;
	}

	/**
	 * Initialization function called by the DTNServer upon, the start service
	 * is requested
	 */
	public static void init(DTNConfiguration dtn) {
		setPolicy(dtn.routes_setting().getQueuing_policy());
	}

	/**
	 * Singleton Implementation Getter function
	 * 
	 * @return an singleton instance of BundleStore
	 */
	public static EpidemicQueuing getInstance() {
		if (instance_ == null) {
			Class myClass;
			try {
				myClass = Class
						.forName("android.geosvr.dtn.servlib.routing.epidemic.queuing."
								+ policy);
				instance_ = (EpidemicQueuing) myClass.newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "Wrong policy");
				e.printStackTrace();
			}
		}
		return instance_;
	}

	public abstract int getLastBundle();

	public boolean removeNextBundle() {
		int bundleID = this.getLastBundle();
		if(bundleID != -1) {
			this.delete(bundleID);
			return true;
		}
		else
			return false;
	}

	public void delete(int bundleid) {
		Log.i(TAG, "Deleting bundle: " + bundleid);
		bundleStore.del(bundleid);
	}

	public void maintainQuota() {
		BundleStore bs = BundleStore.getInstance();
		Log.v(TAG, ""+GlobalStorage.getInstance().get_total_size());
		while(bs.quota() != 0
				&& (GlobalStorage.getInstance().get_total_size() > bs.quota())) {
			if(!this.removeNextBundle())
				break;
		}
	}
}
