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
package android.geosvr.dtn.servlib.routing;

import android.geosvr.dtn.servlib.bundling.event.BundleEvent;


/**
 * This is a non-abstract version of TableBasedRouter. 
 * @author Rerngvit Yanggratoke (rerngvit@kth.se)
 */
public class StaticBundleRouter extends TableBasedRouter implements Runnable{
	
	private BundleEvent event_;
	/**
	 * Thread for running this daemon
	 */
	private Thread thread_;
	/**
	 * Singleton implementation instance
	 */
	private static StaticBundleRouter instance_ = null;
	/**
	 * 单例
	 */
	public static StaticBundleRouter getInstance() {
		if (instance_ == null) {
			instance_ = new StaticBundleRouter();
		}
		return instance_;
	}
	
	/**
	 *  Start a new thread
	 */
	@Override
	public void thread_handle_event(BundleEvent event) {
		event_ = event;
		thread_ = new Thread(this);
		thread_.start();

	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		super.handle_event(event_);
		RouteEventQueue eveQ = RouteEventQueue.getInstance();
//		while(true)
			
	}
}
