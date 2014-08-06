package android.geosvr.dtn.servlib.discovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources.NotFoundException;
import android.geosvr.dtn.DTNService;
import android.geosvr.dtn.R;
import android.geosvr.dtn.servlib.conv_layers.DTNLocationProvider;

public class Location extends TimerTask{
	
	
	private File locationFile_;
	private int timeCounter_;
	private int delay_sec_ ;
	private double longitude_;
	private double latitude_;
	private boolean isManual;
	
	public double getLongitude() {
		return longitude_;
	}
	public double getLatitude() {
		return latitude_;
	}

	private static Location instance_;
	private Location(){
		isManual = false;
		timeCounter_ = 0;
		delay_sec_ = 5;
	}
	public static Location getInstance(){
		if (instance_ == null){
			instance_ = new Location();
		}
		return instance_;
	}
	
	public void init(){
		try {
			if (DTNService.context().getResources().getString(
					R.string.SetLocationManually).equals("true")) {
				isManual = true;
				locationFile_ = new File(DTNService.context().getResources().getString(
						R.string.LocationFilePath));
				Timer timer = new Timer(); 
			    timer.schedule(this, delay_sec_ * 1000, delay_sec_ * 1000);

			} else {
				/*get GPS data*/
			}
			//启动交互接口
			DTNLocationProvider loc = new DTNLocationProvider();
			loc.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//time(sec) longitude latitude
	public void run()
	{
		timeCounter_ += delay_sec_;
		BufferedReader reader = null;
		
//        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
//            reader = new BufferedReader(new FileReader(locationFile_));
//            String readStr = null;
//            String tempStr[] = null;
//            
//            //每行格式:时间（sec） 经度  纬度
//            // 读到对应的一行写入
//            while ((readStr = reader.readLine()) != null) {
//            	tempStr = readStr.split(" ");
//            	if (Integer.parseInt(tempStr[0]) >= timeCounter_) {
//            		longitude_ = Integer.parseInt(tempStr[1]);
//            		latitude_ = Integer.parseInt(tempStr[2]);
//            		break;
//            	}
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                }
//            }
//        }
        
		longitude_ = 100.112233;
		latitude_ = 200.556677;
	}
	

}
