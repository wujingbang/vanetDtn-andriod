/*
 *	  This file is part of the Bytewalla Project
 *    More information can be found at "http://www.tslab.ssvl.kth.se/csd/projects/092106/".
 *    
 *    Copyright 2009 Telecommunication Systems Laboratory (TSLab), Royal Institute of Technology, Sweden.
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 */

package android.geosvr.dtn.servlib.discovery;

import java.util.HashMap;
import java.util.Iterator;

import android.geosvr.dtn.DTNManager;
import android.geosvr.dtn.servlib.bundling.Bundle;
import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundlePayload.location_t;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkStateChangeRequest;
import android.geosvr.dtn.servlib.contacts.ContactManager;
import android.geosvr.dtn.servlib.contacts.Link;
import android.geosvr.dtn.servlib.conv_layers.ConvergenceLayer;
import android.geosvr.dtn.servlib.naming.EndpointID;
import android.util.Log;

/**
 * "Abstraction of neighbor discovery agent.
 * 
 * Much like Interface, Discovery is generally created by the configuration file
 * / console. Derived classes (such as IPDiscovery) typically bind to a UDP
 * socket to listen for neighbor beacons. Bluetooth has built-in discovery
 * mechanisms, so BluetoothDiscovery polls via Inquiry instead of listen()ing on
 * a socket.
 * 
 * To advertise a local convergence layer, register its local address (and port)
 * by calling "discovery add_cl". For each registered CL, Discovery will
 * advertise (outbound) the CL's presence to neighbors, and distribute (inbound)
 * each event of neighbor discovery to each CL" [DTN2].
 * 
 * @author Mar�a Jos� Peroza Marval (mjpm@kth.se)
 */

public abstract class Discovery {

	/**
	 * TAG for Android Logging mechanism
	 */
	private static final String TAG = "Discovery";
	private static HashMap<String, String> discoveries = new HashMap<String, String>();
	
	public static HashMap<String, String> discoveries()
	{
		return discoveries;
	}

	/**
	 * Constructor
	 */
	public Discovery(String name, String af) {
		name_ = name;
		af_ = af;

	}

	/**
	 * Name of this Discovery instance
	 */
	public String name() {
		return name_;
	}

	/**
	 * Address family represented by this Discovery instance
	 */
	public String af() {
		return af_;
	}

	/**
	 * Outbound address of advertisements sent by this Discovery instance
	 */
	public String to_addr() {
		return to_addr_;
	}

	/**
	 * Local address on which to listen for advertisements
	 */
	public String local_addr() {
		return local_;
	}

	/**
	 * Factory method for instantiating objects from the appropriate derived
	 * class
	 * 工厂设计模式
	 */
	public static Discovery create_discovery(String name, String afname,
			short port) {

		list_ = new AnnouncementList();

		Discovery disc;
		if (afname == "ip") {
			disc = new IPDiscovery(name, port);
		} else {
			// not a recognized address family
			Log.e(TAG, "unknown address family");
			disc = null;

		}

		if (!disc.configure()) {
			disc = null;
		}

		return disc;
	}

	public abstract void start();

	/**
	 * Append snapshot of object state to StringBuffer
	 */
	public void dump(StringBuffer buf) {

		String LongString = name_.concat(af_);
		LongString = LongString.concat(to_addr_);

		buf.append(LongString);
		buf.append(list_.size());

		Iterator<Announce> i = list_.iterator();
		while (i.hasNext()) {

			Announce element = i.next();

			LongString = element.name().concat(element.type());
			LongString = LongString.concat(element.local_addr());
			buf.append(LongString);
			buf.append(element.interval());

		}

	}

	/**
	 * Close down listening socket and stop the thread. Derived classes should
	 * NOT auto-delete.
	 */
	public abstract void shutdown();

	/**
	 * Register an Announce to advertise a local convergence layer and to
	 * respond to advertisements from neighbors
	 *///!disc.announce(AnnounceID, code, ClType, interval)
	public boolean announce(String name, int argc, String ClType, int interval) {

		if (list_.indexOf(name) != -1) {
			Log.e(TAG, "discovery for name already exists");
			return false;
		}

		if (argc < 1) {
			Log.e(TAG, "cl type not specified");
			return false;
		}

		String cltype = ClType;
		//ConvergenceLayer 在init时，add了一个UDP。
		ConvergenceLayer cl = ConvergenceLayer.find_clayer(cltype);
		if (cl == null) {
			Log.e(TAG, "invalid convergence layer type");
			return false;
		}

		Announce announce = Announce.create_announce(name, cl, argc, ClType,
				interval);
		if (announce == null) {
			Log
					.e(TAG,
							"no announce implemented for This type of convergence layer");
			return false;
		}

		list_.add(announce);

		return true;

	}

	/**
	 * Remove registration for named announce object
	 */
	public boolean remove(String name) {

		Iterator<Announce> i = list_.iterator();

		if (!find(name, i)) {
			Log.e(TAG, "error removing announce,no such object");
			return false;
		}

		while (i.hasNext()) {

			Announce element = i.next();

			if (element.name() == name) {
				i.remove();

			}
		}
		return true;
	}

	/**
	 * Handle neighbor discovery out to registered DiscoveryInfo objects
	 */
	public void handle_neighbor_discovered(String cl_type, String cl_addr,
			EndpointID remote_eid) {

		BundleDaemon Daemon = BundleDaemon.getInstance();

		ContactManager cm = Daemon.contactmgr();
		
//		ConvergenceLayer cl = ConvergenceLayer.find_clayer(cl_type);
//		if (cl == null) {
//			Log.e(TAG, "unknown convergence layer type");
//			return;
//		}

		// Look for match on convergence layer and remote EID
		cm.get_lock().lock();
		Link link = cm.find_link_to(remote_eid);

		if (link == null) {
			//在这里post了link_created事件
			link = cm.new_opportunistic_link(
					(link==null) ? ConvergenceLayer.find_clayer(cl_type) : link.clayer(), 
							cl_addr, remote_eid);
			
			if (link == null) {
				Log.d(TAG, "failed to create opportunistic link");
				return;
			}

/*
			Bundle bundle = new Bundle(location_t.MEMORY);
			bundle.set_dest(remote_eid);
			bundle.set_source(BundleDaemon.getInstance().local_eid());
			bundle.get_lock().lock();
			link.queue().insert_random(bundle);
			bundle.get_lock().unlock();
*/
			
//			if(discoveries.get(remote_eid.str()).equals(cl_addr)){
//			}
//			else {
//				discoveries.remove(remote_eid.str());
//				discoveries.put(remote_eid.str(), cl_addr);
//				DTNManager.getInstance().notify_user("New peer discovered", remote_eid.str());
//			}

            if(discoveries.containsKey(remote_eid.str())) {
                    if(discoveries.get(remote_eid.str()).equals(cl_addr)){
                    }
                    else {
                            discoveries.remove(remote_eid.str());
                            discoveries.put(remote_eid.str(), cl_addr);
                            DTNManager.getInstance().notify_user("New peer discovered", remote_eid.str());
                    }
            }
            else {
                    discoveries.put(remote_eid.str(), cl_addr);
                    DTNManager.getInstance().notify_user("New peer discovered", remote_eid.str());
            }
			

			BundleDaemon BD = BundleDaemon.getInstance();
			// request to set link available
			BD.post(new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
					ContactEvent.reason_t.DISCOVERY));

		}
		else {
			assert (link != null);
			if (!link.isNotUnavailable()) {
				link.lock().lock();
				link.set_nexthop(cl_addr);
				link.lock().unlock();

				BundleDaemon BD = BundleDaemon.getInstance();
				// request to set link available
				BD.post(new LinkStateChangeRequest(link, Link.state_t.AVAILABLE,
						ContactEvent.reason_t.DISCOVERY));
			}
		}
		cm.get_lock().unlock();
	}

	/**
	 * Configure this Discovery instance
	 */
	protected abstract boolean configure();

	/**
	 * Find a registration by name
	 */
	protected boolean find(String name, Iterator<Announce> iter) {

		Iterator<Announce> i = list_.iterator();
		while (i.hasNext()) {

			Announce element = i.next();

			if (element.name() == name) {
				return true;
			}
		}

		return false;

	}

	//wujingbang
	private String tlprotocol_; //传输层是tcp还是udp？
	private String name_; // /< name of discovery agent
	private String af_; // /< address family
	protected String to_addr_; // /< outbound address of advertisements sent
	protected String local_; // /< address of beacon listener
	protected static AnnouncementList list_; // /< registered Announce objects

}
