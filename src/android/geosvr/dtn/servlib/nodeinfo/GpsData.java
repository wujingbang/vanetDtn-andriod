///*
// * GPS信息：地理位置、速度、方向、状态（静止/运动）、给系统指定的目的地；
// */
//package android.geosvr.dtn.servlib.nodeinfo;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.location.LocationManager;
//import android.os.IBinder;
//
//public class GpsData extends Service implements Runnable {
//	private double 	Speed;
//	private double 	Longitude;
//	private double 	Latitude;
//	private double 	LoctionZ;
//	private double 	VectorX; //方向矢量
//	private double 	VectorY;
//	private double 	VectorZ;
//	private int		State; //节点状态
//	private double	Destnation;
//	
//	private LocationManager locationManager;
//	
//	private static GpsData instance;
//	private Thread thread_;
//	
//	public GpsData() {
//		Speed = 0.0;
//		Longitude = 0.0;
//		Latitude = 0.0;
//		LoctionZ = 0.0;
//		VectorX = 0.0;
//		VectorY = 0.0;
//		VectorZ = 0.0;
//		State = 0;
//		Destnation = 0.0;
//		
//		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//	}
//	
//	public static GpsData getInstance(){
//		if(instance == null){
//			synchronized(GpsData.class){
//				if(instance == null){
//					instance = new GpsData();
//				}
//			}
//		}
//		return instance;
//	}
//	
//	/**
//	 *  Start the Gps Daemon by executing a new thread
//	 */
//	public void start() {
//		thread_ = new Thread(this);
//		thread_.start();
//
//	}
//	
//	@Override
//	public void run() {
////		try{
////			//从GPS取得各种信息，计算效用值等。
////			
////		}
//		
//	}
//	
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	void decay(){
////		setUtilValue_x(getUtilValue_x()*CON_DERATE);
////		setUtilValue_y(getUtilValue_y()*CON_DERATE);
//	}
//
//	/*
//	*
//	*/
//	void utility_resolove(){
//
//		//获取节点速度和方向矢量；同时更新本节点的速度和位置
////		Node *thisnode; //
////		thisnode = Node::get_node_by_address(index);//index);
////		speed = ((MobileNode *)thisnode)->speed();
////		//方向矢量
////		((MobileNode *)thisnode)->getVelo(&orientX, &orientY, &orientZ);
////		//地址
////		((MobileNode *)thisnode)->getLoc(&locX, &locY, &locZ);
////		double incr_x, incr_y;
////		if(speed == 0){
////			incr_x = 0.0;
////			incr_y = 0.0;
////		}else{
////			incr_x = speed * SPEED_PARA * orientX;//Y/sqrt(pow(orientX,2)+pow(orientY,2)));
////			incr_y = speed * SPEED_PARA * orientY;///sqrt(pow(orientX,2)+pow(orientY,2)));
////		}
////		//迅速降低反方向的值
////		if(getUtilValue_x() * incr_x < 0)
////			incr_x *= 2;
////		if(getUtilValue_y() * incr_y < 0)
////			incr_y *= 2;
////
////		setUtilValue_x(getUtilValue_x() + incr_x);
////		setUtilValue_y(getUtilValue_y() + incr_y);
////
////		//decrease
////		decay();
//	}
//
//	/*
//	 * 	1. 有自己的坐标
//	 *	2. 目的地坐标作为参数
//	 *	3. 返回合成后的值用于对比
//	 */
//	double utility_combin(double inter_angle_cos, double utility_x, double utility_y){
//		return sqrt(pow(utility_x, 2) + pow(utility_y, 2)) * inter_angle_cos;
//	}
//
//	double inter_angle_cos(double dst_locX, double dst_locY, double node_locX, double node_locY){
////		long double a = pow(dst_locX - node_locX, 2) + pow(dst_locY - node_locY, 2);
////		long double b = pow(dst_locX,2)+pow(dst_locY,2);
////		long double c = pow(node_locX,2)+pow(node_locY,2);
////
////		return ((b+c-a)/(2*sqrt(b)*sqrt(c)));
//
//	}
//
//	
//	public double getSpeed() {
//		return Speed;
//	}
//	public void setSpeed(double speed) {
//		Speed = speed;
//	}
//	public double getLongitude() {
//		return Longitude;
//	}
//	public void setLongitude(double longitude) {
//		Longitude = longitude;
//	}
//	public double getLatitude() {
//		return Latitude;
//	}
//	public void setLatitude(double latitude) {
//		Latitude = latitude;
//	}
//	public double getLoctionZ() {
//		return LoctionZ;
//	}
//	public void setLoctionZ(double loctionZ) {
//		LoctionZ = loctionZ;
//	}
//	public double getVectorX() {
//		return VectorX;
//	}
//	public void setVectorX(double vectorX) {
//		VectorX = vectorX;
//	}
//	public double getVectorY() {
//		return VectorY;
//	}
//	public void setVectorY(double vectorY) {
//		VectorY = vectorY;
//	}
//	public double getVectorZ() {
//		return VectorZ;
//	}
//	public void setVectorZ(double vectorZ) {
//		VectorZ = vectorZ;
//	}
//	public int getState() {
//		return State;
//	}
//	public void setState(int state) {
//		State = state;
//	}
//	public double getDestnation() {
//		return Destnation;
//	}
//	public void setDestnation(double destnation) {
//		Destnation = destnation;
//	}
//
//
//
//
//
//}
