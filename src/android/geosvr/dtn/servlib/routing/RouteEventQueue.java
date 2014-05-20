package android.geosvr.dtn.servlib.routing;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.geosvr.dtn.servlib.bundling.event.BundleEvent;
import android.util.Log;

public class RouteEventQueue {
	private Lock lock_ = new ReentrantLock();
	private Condition condition_pro_ = lock_.newCondition();
	private Condition condition_con_ = lock_.newCondition();
	
	BundleEvent[] eventQueue_;
    int front_;  //队首下标   
    int rear_;   //队尾下标   
    int size_ = 100;
    
	//单例
	private RouteEventQueue(){
		eventQueue_ = new BundleEvent[size_];
        front_ = 0;   
        rear_ =0;  
	}
	private static RouteEventQueue instance_;
	public static RouteEventQueue getInstance(){
		if (instance_ == null)
			instance_ = new RouteEventQueue();
		return instance_;
	}
	


	protected boolean queueFull() {
		return ((rear_+1) % size_ == front_);
	}
	
	protected boolean queueEmpty() {
		return (rear_ == front_);
	}
    /**  
     * 将一个对象追加到队列尾部  
     * @param obj 对象  
     * @return 队列满时返回false,否则返回true  
     */  
	protected boolean enqueue(BundleEvent event){   
//        if(((rear_+1) % size_) == front_)
    	if (queueFull()) 
            return false;   
    	eventQueue_[rear_] = event;   
        rear_ = (rear_+1) % size_;   
        Log.e("QUEUE", "queue tail is "+rear_);
        return true;   
    }   
    /**  
     * 队列头部的第一个对象出队  
     * @return 出队的对象，队列空时返回null  
     */  
	protected BundleEvent dequeue(){   
//        if(rear_ == front_){ 
		if (queueEmpty())
            return null; 
        BundleEvent segment = eventQueue_[front_];   
        front_ = (front_+1) % size_; 
        Log.e("QUEUE", "queue front_ is "+front_);
        return segment;   
    }   
	
	public void incease(BundleEvent event){
		
	}
	
//	public
}
