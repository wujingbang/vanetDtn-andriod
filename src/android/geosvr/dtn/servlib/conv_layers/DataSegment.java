package android.geosvr.dtn.servlib.conv_layers;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.geosvr.dtn.servlib.bundling.BundlePayload;
import android.geosvr.dtn.systemlib.util.IByteBuffer;

public class DataSegment {
	private byte[] tempStora_;
	private int offset_;
	private int len_;
	private boolean flag_ = false;
	
	//检查所用
	private boolean bundle_complete_flag_ = false;
	public IncomingBundle incoming_;
    //用于检查Bundle是否接收完成
    public Connection connection_;
    
//	private RandomAccessFile file_handle_;
	private BundlePayload payload_;
	private Lock lock_ = new ReentrantLock();
	
	public void setBundleCompleteFlag(boolean isCompete, IncomingBundle incoming, Connection connection){
		bundle_complete_flag_ = isCompete;
		incoming_ = incoming;
		connection_ = connection;
	}
	public boolean getBundleCompleteFlag(){
		return bundle_complete_flag_;
	}
	
	public void set(IByteBuffer src, int offset, int len, 
//			RandomAccessFile file_handle){
			BundlePayload payload) {
//		lock_.lock();
		src.mark();
		try {
			tempStora_ = new byte[len];
			offset_ = offset;
			len_ = len;
			src.get(tempStora_);
//			file_handle_ = file_handle;
			payload_ = payload;
			flag_ = true;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			src.reset();
//			lock_.unlock();
		}
	}
	

	
	public void writeToFile(){
		try {
//			while(!flag_)
//				condition_con_.await();
			assert(flag_ == true):"Consumer Error!!";
			FileOutputStream fos = new FileOutputStream(payload_.file(), true);
			fos.write(tempStora_);
			fos.close();
			flag_ = false;
//			condition_pro_.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			lock_.unlock();
		}
	}
	
//	public void writeToFile(){
////		lock_.lock();
//		try {
////			while(!flag_)
////				condition_con_.await();
//			assert(flag_ == true):"Consumer Error!!";
//			file_handle_.seek(offset_);
//			file_handle_.write(tempStora_);
//			file_handle_.close();
//			
//			flag_ = false;
////			condition_pro_.signal();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
////			lock_.unlock();
//		}
//	}
}