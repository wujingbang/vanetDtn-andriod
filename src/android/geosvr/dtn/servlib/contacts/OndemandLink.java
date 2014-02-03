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
import android.geosvr.dtn.servlib.bundling.event.LinkAvailableEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent.reason_t;
import android.geosvr.dtn.servlib.conv_layers.ConvergenceLayer;

/**
 * "Abstraction for a ONDEMAND link.
 * 
 * ONDEMAND links have to be opened every time one wants to use it and close
 * after an idle period" [DTN2].
 * 
 * @author Mar�a Jos� Peroza Marval (mjpm@kth.se)
 */

public class OndemandLink extends Link {

	/**
	 * Unique identifier according to Java Serializable specification
	 */
	private static final long serialVersionUID = 8419300085330455289L;

	/*
	 * Constructor
	 */
	public OndemandLink(String name, ConvergenceLayer cl, String nexthop) {

		super(name, Link.link_type_t.ONDEMAND, cl, nexthop);
		set_state(Link.state_t.AVAILABLE);

		// override the default for the idle close time
//		params_.set_idle_close_time(30000);
	}

	/**
	 * This function changes the state of the link
	 */
	@Override
	public void set_initial_state() {

		BundleEvent event = new LinkAvailableEvent(this, reason_t.NO_INFO);
		BundleDaemon BD = BundleDaemon.getInstance();
		BD.post(event);

	}

}
