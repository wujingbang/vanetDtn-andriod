package android.geosvr.dtn.servlib.routing;

import java.util.Iterator;

import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundleInfoCache;
import android.geosvr.dtn.servlib.bundling.BundleProtocol;
import android.geosvr.dtn.servlib.bundling.ForwardingInfo;
import android.geosvr.dtn.servlib.bundling.event.BundleAcceptRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleCancelRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleDeleteRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleDeliveredEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleExpiredEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleFreeEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleInjectRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleInjectedEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleQueuedQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleQueuedReportEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleReceivedEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleReportEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleSendCancelledEvent;
import android.geosvr.dtn.servlib.bundling.event.BundleSendRequest;
import android.geosvr.dtn.servlib.bundling.event.BundleTransmittedEvent;
import android.geosvr.dtn.servlib.bundling.event.CLAParametersQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.CLAParametersReportEvent;
import android.geosvr.dtn.servlib.bundling.event.CLAParamsSetEvent;
import android.geosvr.dtn.servlib.bundling.event.CLASetParamsRequest;
import android.geosvr.dtn.servlib.bundling.event.ContactAttributeChangedEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactDownEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.ContactReportEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactUpEvent;
import android.geosvr.dtn.servlib.bundling.event.CustodySignalEvent;
import android.geosvr.dtn.servlib.bundling.event.CustodyTimeoutEvent;
import android.geosvr.dtn.servlib.bundling.event.EIDReachableQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.EIDReachableReportEvent;
import android.geosvr.dtn.servlib.bundling.event.IfaceAttributesQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.IfaceAttributesReportEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkAttributeChangedEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkAttributesQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.LinkAttributesReportEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkAvailableEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkCreatedEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkDeleteRequest;
import android.geosvr.dtn.servlib.bundling.event.LinkDeletedEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.LinkReconfigureRequest;
import android.geosvr.dtn.servlib.bundling.event.LinkReportEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkStateChangeRequest;
import android.geosvr.dtn.servlib.bundling.event.LinkUnavailableEvent;
import android.geosvr.dtn.servlib.bundling.event.NewEIDReachableEvent;
import android.geosvr.dtn.servlib.bundling.event.ReassemblyCompletedEvent;
import android.geosvr.dtn.servlib.bundling.event.RegistrationAddedEvent;
import android.geosvr.dtn.servlib.bundling.event.RegistrationDeleteRequest;
import android.geosvr.dtn.servlib.bundling.event.RegistrationExpiredEvent;
import android.geosvr.dtn.servlib.bundling.event.RegistrationRemovedEvent;
import android.geosvr.dtn.servlib.bundling.event.RouteAddEvent;
import android.geosvr.dtn.servlib.bundling.event.RouteDelEvent;
import android.geosvr.dtn.servlib.bundling.event.RouteQueryRequest;
import android.geosvr.dtn.servlib.bundling.event.RouteReportEvent;
import android.geosvr.dtn.servlib.bundling.event.SetLinkDefaultsRequest;
import android.geosvr.dtn.servlib.bundling.event.ShutdownRequest;
import android.geosvr.dtn.servlib.bundling.event.StatusRequest;
import android.geosvr.dtn.servlib.config.DTNConfiguration;
import android.geosvr.dtn.servlib.contacts.Link;
import android.geosvr.dtn.servlib.naming.EndpointID;
import android.geosvr.dtn.servlib.naming.EndpointIDPattern;
import android.util.Log;

public class GeosvrDtnRouter extends BundleRouter {
	/**
	 * TAG name for logging in Android Logging system
	 */
	private final static String TAG = "GeosvrDtnRouter";
	/**
	 *  "Cache to check for duplicates and to implement a simple RPF check" [DTN2]
	 */
	private BundleInfoCache reception_cache_;
	
	/**
	 * constructor
	 */
	public GeosvrDtnRouter() {
		
	}

	@Override
	public void initialize(DTNConfiguration dtn_config_) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete_bundle(Bundle bundle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get_routing_state(StringBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recompute_routes() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * "Check the route table entries that match the given bundle and have not
	 * already been found in the bundle history. If a match is found, call
	 * fwd_to_nexthop on it." [DTN2]
	 * 
	 * @param bundle
	 *            the bundle to forward
	 * 
	 * @return "the number of links on which the bundle was queued
	 *            (i.e. the number of matching route entries.)" [DTN2]
	 */
	protected int route_bundle(Bundle bundle) {
		return 0 ;
//
//		RouteEntryVec matches = new RouteEntryVec();
//
//		Log.d(TAG, String.format("route_bundle: checking bundle %d", bundle
//				.bundleid()));
//
//		// "check to see if forwarding is suppressed to all nodes" [DTN2]
//		if (bundle.fwdlog().get_count(EndpointIDPattern.WILDCARD_EID(),
//				ForwardingInfo.state_t.SUPPRESSED.getCode(),
//				ForwardingInfo.ANY_ACTION) > 0) {
//			Log.i(TAG, String.format("route_bundle: "
//					+ "ignoring bundle %d since forwarding is suppressed",
//					bundle.bundleid()));
//			return 0;
//		}
//		
//		//数据包目的地广播给邻居节点
//
//		Link null_link = null;
//		route_table_.get_matching(bundle.dest(), null_link, matches);
//
//		// "sort the matching routes by priority, allowing subclasses to
//		// override the way in which the sorting occurs" [DTN2]
//		sort_routes(matches);
//
//		Log.d(TAG, String.format(
//				"route_bundle bundle id %d: checking %d route entry matches",
//				bundle.bundleid(), matches.size()));
//
//		int count = 0;
//		Iterator<RouteEntry> itr = matches.iterator();
//		while (itr.hasNext()) {
//			RouteEntry route = itr.next();
//			Log.d(TAG, String.format("checking route entry %s link %s (%s)",
//					route.toString(), route.link().name(), route.link()));
//
//			if (!should_fwd(bundle, route)) {
//				continue;
//			}
//
//			if (deferred_list(route.link()).list().contains(bundle)) {
//				Log.d(TAG, String.format("route_bundle bundle %d: "
//						+ "ignoring link %s since already deferred", bundle
//						.bundleid(), route.link().name()));
//				continue;
//			}
//
//			// "because there may be bundles that already have deferred
//			// transmission on the link, we first call check_next_hop to
//			// get them into the queue before trying to route the new
//			// arrival, otherwise it might leapfrog the other deferred
//			// bundles" [DTN2]
//			check_next_hop(route.link());
//
//			if (!fwd_to_nexthop(bundle, route)) {
//				continue;
//			}
//
//			++count;
//		}
//
//		Log.d(TAG, String.format(
//				"route_bundle bundle id %d: forwarded on %d links", bundle
//						.bundleid(), count));
//		return count;
	}

	/**
	 * 
	 */
	@Override
	public void handle_event(BundleEvent event) {
		//转到基类BundleEventHandler分发事件。
		dispatch_event(event);

	}

	@Override
	protected void handle_bundle_received(BundleReceivedEvent event) {
		// 
		boolean should_route = true;

		Bundle bundle = event.bundle();
		Log.d(TAG, String.format("handle bundle received: bundle %d", bundle.bundleid()));

		EndpointID remote_eid = EndpointID.NULL_EID();

		if (event.link() != null) {
			remote_eid = event.link().remote_eid();
		}

		if (!reception_cache_.add_entry(bundle, remote_eid)) {
			Log.i(TAG, String.format("ignoring duplicate bundle: bundle %d", bundle.bundleid()));
			BundleDaemon
					.getInstance()
					.post_at_head(
							new BundleDeleteRequest(
									bundle,
									BundleProtocol.status_report_reason_t.REASON_NO_ADDTL_INFO));
			return;
		}

		if (should_route) {
			route_bundle(bundle);
		} else {
			BundleDaemon
					.getInstance()
					.post_at_head(
							new BundleDeleteRequest(
									bundle,
									BundleProtocol.status_report_reason_t.REASON_NO_ADDTL_INFO));
		}
	}

	@Override
	protected void handle_bundle_transmitted(BundleTransmittedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_delivered(BundleDeliveredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_expired(BundleExpiredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_free(BundleFreeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_send(BundleSendRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_cancel(BundleCancelRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_cancelled(BundleSendCancelledEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_inject(BundleInjectRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_injected(BundleInjectedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_delete(BundleDeleteRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_accept(BundleAcceptRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_query(BundleQueryRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_report(BundleReportEvent request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_registration_added(RegistrationAddedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_registration_removed(RegistrationRemovedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_registration_expired(RegistrationExpiredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_registration_delete(RegistrationDeleteRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_contact_up(ContactUpEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_contact_down(ContactDownEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_contact_query(ContactQueryRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_contact_report(ContactReportEvent request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_contact_attribute_changed(
			ContactAttributeChangedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_created(LinkCreatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_deleted(LinkDeletedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_available(LinkAvailableEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_unavailable(LinkUnavailableEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_state_change_request(LinkStateChangeRequest req) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_delete(LinkDeleteRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_reconfigure(LinkReconfigureRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_query(LinkQueryRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_report(LinkReportEvent request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_attribute_changed(LinkAttributeChangedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_reassembly_completed(ReassemblyCompletedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_route_add(RouteAddEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_route_del(RouteDelEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_route_query(RouteQueryRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_route_report(RouteReportEvent request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_custody_signal(CustodySignalEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_custody_timeout(CustodyTimeoutEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_shutdown_request(ShutdownRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_status_request(StatusRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_cla_set_params(CLASetParamsRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_cla_params_set(CLAParamsSetEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_set_link_defaults(SetLinkDefaultsRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_new_eid_reachable(NewEIDReachableEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_queued_query(BundleQueuedQueryRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_bundle_queued_report(BundleQueuedReportEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_eid_reachable_query(EIDReachableQueryRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_eid_reachable_report(EIDReachableReportEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_attributes_query(LinkAttributesQueryRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_link_attributes_report(LinkAttributesReportEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_iface_attributes_query(
			IfaceAttributesQueryRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_iface_attributes_report(
			IfaceAttributesReportEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_cla_parameters_query(CLAParametersQueryRequest event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handle_cla_parameters_report(CLAParametersReportEvent event) {
		// TODO Auto-generated method stub

	}

}
