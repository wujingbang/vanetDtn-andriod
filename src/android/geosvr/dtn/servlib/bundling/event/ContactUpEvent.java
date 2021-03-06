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

package android.geosvr.dtn.servlib.bundling.event;

import android.geosvr.dtn.servlib.contacts.Contact;

/**
 * Event class for contact up events. This means the DTN Contact Header is exchanged between this node and the other side.
 * @author Rerngvit Yanggratoke (rerngvit@kth.se)   
 */
public class ContactUpEvent extends ContactEvent {

	/**
	 * Constructor
	 * @param contact
	 */
	public ContactUpEvent(Contact contact) {
		super(event_type_t.CONTACT_UP, ContactEvent.reason_t.NO_INFO);
		contact_ = contact;

	}

	/**
	 *  The contact that is up
	 */
	private Contact contact_;
	/**
	 * Getter for the contact that is up
	 * @return
	 */
	public Contact contact() { return contact_; }
	/**
	 * Setter for the contact that is up
	 * @param contact
	 */
	public void set_contact(Contact contact) { contact_ = contact; }
};
