package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.geosvr.dtn.servlib.bundling.BundleDaemon;
import android.geosvr.dtn.servlib.bundling.BundlePayload;
import android.geosvr.dtn.servlib.bundling.BundleProtocol;
import android.geosvr.dtn.servlib.bundling.event.BundleReceivedEvent;
import android.geosvr.dtn.servlib.bundling.event.ContactEvent;
import android.geosvr.dtn.servlib.bundling.event.event_source_t;
import android.geosvr.dtn.servlib.contacts.Contact;
import android.geosvr.dtn.systemlib.util.BufferHelper;
import android.geosvr.dtn.systemlib.util.IByteBuffer;
import android.util.Log;

public class Resource {	
	private Lock lock_ = new ReentrantLock();
	private Condition condition_pro_ = lock_.newCondition();
	private Condition condition_con_ = lock_.newCondition();
	
	DataSegment[] dataSegments_; //对象数组，队列最多存储a.length-1个对象   
    int front_;  //队首下标   
    int rear_;   //队尾下标   
    int size_ = 3000;
    

    
	//单例
	private Resource(){
		dataSegments_ = new DataSegment[size_];   
        front_ = 0;   
        rear_ =0;  
	}
	private static Resource instance_;
	public static Resource getInstance(){
		if (instance_ == null)
			instance_ = new Resource();
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
	protected boolean enqueue(DataSegment segment){   
//        if(((rear_+1) % size_) == front_)
    	if (queueFull()) 
            return false;   
        dataSegments_[rear_] = segment;   
        rear_ = (rear_+1) % size_;   
//        Log.e("QUEUE", "queue tail is "+rear_);
        return true;   
    }   
    /**  
     * 队列头部的第一个对象出队  
     * @return 出队的对象，队列空时返回null  
     */  
	protected DataSegment dequeue(){   
//        if(rear_ == front_){ 
		if (queueEmpty())
            return null;
        DataSegment segment = dataSegments_[front_];   
        front_ = (front_+1) % size_; 
//        Log.e("QUEUE", "queue front_ is "+front_);
        return segment;   
    }   
	
	public void increase(IByteBuffer src, int offset, int len, 
//			RandomAccessFile file_handle) {
			BundlePayload payload) {
//		lock_.lock();
		try {
			while(queueFull()) {
				Thread.sleep(100);
//				condition_pro_.await();//队列满，等待消费者
			}
			DataSegment segment = new DataSegment();
			segment.set(src, offset, len, payload);
			enqueue(segment);//入队
//			condition_con_.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
//			lock_.unlock();
		}
	}
	
	//插入一个checkBundleComplete条目
	public void increase(IncomingBundle incoming, Connection connection){
		try {
			while(queueFull()) {
				Thread.sleep(100);
//				condition_pro_.await();//队列满，等待消费者
			}
			DataSegment segment = new DataSegment();
			segment.setBundleCompleteFlag(true, incoming, connection);
			enqueue(segment);//入队
//			condition_con_.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
//			lock_.unlock();
		}
	}
    
	public void decreaseAndWriteFile(){
//		lock_.lock();		
    	try {
			while(queueEmpty()) {
//				condition_con_.await();//队列空，等待生产者
				Thread.sleep(100);
			}
			DataSegment segment = dequeue();
			if (segment == null)
				return;
			if (segment.getBundleCompleteFlag()){
				//文件已经在前面的队列中写完了,所以post以后直接return
				segment.connection_.postCompeteBundle(segment.incoming_);
				Log.d("Resource", "**********Bundle Complete************");
				return;
			}
//			condition_pro_.signal();
//			lock_.unlock();
			segment.writeToFile();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
//			lock_.unlock();
		}
    }
	

//	public void set(IByteBuffer src, int offset, int len, RandomAccessFile file_handle){
//	lock_.lock();
//	src.mark();
//	try {
//		while(flag_)
//			condition_pro_.await();
//		tempStora_ = new byte[len];
//		offset_ = offset;
//		len_ = len;
//		src.get(tempStora_);
//		file_handle_ = file_handle;
//		
//		flag_ = true;
//		condition_con_.signal();
//	} catch(Exception e) {
//		e.printStackTrace();
//	} finally {
//		src.reset();
//		lock_.unlock();
//	}
//}
	
}
