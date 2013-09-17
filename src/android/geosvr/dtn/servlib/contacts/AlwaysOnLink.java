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

package android.geosvr.dtn.servlib.contacts;

import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.event.BundleEvent;
import android.geosvr.dtn.servlib.bundling.event.LinkStateChangeRequest;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent.reason_t;
import android.geosvr.dtn.servlib.conv_layers.ConvergenceLayer;

/**
 * "ALWAYSON links are immediately opened upon creation and remain open for
 * their duration" [DTN2].
 * 
 * @author Mar�a Jos� Peroza Marval (mjpm@kth.se)
 */

public class AlwaysOnLink extends Link {

	/**
	 * Unique identifier according to Java Serializable specification
	 */
	private static final long serialVersionUID = -8238280579978005251L;

	/**
	 * Constructor
	 */
	public AlwaysOnLink(String name, ConvergenceLayer cl, String nexthop) {

		super(name, Link.link_type_t.ALWAYSON, cl, nexthop);
		set_state(Link.state_t.UNAVAILABLE);
	}

	/**
	 * This function changes the state of the link to OPEN
	 */

	@Override
	public void set_initial_state() {

		BundleEvent event = new LinkStateChangeRequest(this, state_t.OPEN,
				reason_t.USER);
		BundleDaemon BD = BundleDaemon.getInstance();
		BD.post_at_head(event);

	}
}
