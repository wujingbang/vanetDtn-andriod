package android.geosvr.dtn.servlib.conv_layers;

import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConsumerFileWriter implements Runnable {
	private Thread thread_;
	private static Lock lock_ = new ReentrantLock();
	
	
	//单例
//	private ConsumerFileWriter (){
//		
//	}
//	private static ConsumerFileWriter instance_;
//	public static ConsumerFileWriter getInstance(RandomAccessFile file_handle){
//		lock_.lock();
//		if(instance_==null) 
//			instance_ = new ConsumerFileWriter();
//		
//		return instance_;
//	}
	
	public void init(){
		thread_ = new Thread(this);
	}
	
	public void start(){
		init();
		thread_.start();
	}
	
	@Override
	public void run() {
		Resource res = Resource.getInstance();
		while(true)
			res.decreaseAndWriteFile();
//			res.writeToFile();
	}

}
