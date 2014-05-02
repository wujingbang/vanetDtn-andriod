package android.geosvr.dtn.servlib.conv_layers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.geosvr.dtn.systemlib.util.BufferHelper;
import android.geosvr.dtn.systemlib.util.IByteBuffer;

public class Resource {
	private byte[] tempStora_;
	private int offset_;
	private int len_;
	private boolean flag_ = false;
	private RandomAccessFile file_handle_;
	
	private Lock lock_ = new ReentrantLock();
	private Condition condition_pro_ = lock_.newCondition();
	private Condition condition_con_ = lock_.newCondition();
	
	//单例
	private Resource(){
		
	}
	private static Resource instance_;
	public static Resource getInstance(){
		if (instance_ == null)
			instance_ = new Resource();
		return instance_;
	}
	
	
	public void set(IByteBuffer src, int offset, int len, RandomAccessFile file_handle){
		lock_.lock();
		src.mark();
		try {
			while(flag_)
				condition_pro_.await();
			tempStora_ = new byte[len];
			offset_ = offset;
			len_ = len;
			src.get(tempStora_);
			file_handle_ = file_handle;
			
			flag_ = true;
			condition_con_.signal();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			src.reset();
			lock_.unlock();
		}
	}
	
	public void writeToFile(){
		lock_.lock();
		try {
			while(!flag_)
				condition_con_.await();
			file_handle_.seek(offset_);
			file_handle_.write(tempStora_);
			file_handle_.close();
			
			flag_ = false;
			condition_pro_.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock_.unlock();
		}
	}
	
}
