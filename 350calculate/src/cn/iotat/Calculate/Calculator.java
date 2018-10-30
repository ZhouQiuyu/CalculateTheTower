package cn.iotat.Calculate;

import cn.iotat.Constants.Constants;
import cn.iotat.SerialPort.SerialPort;
import cn.iotat.View.View;
import gnu.io.PortInUseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 所有的计算功能在这里实现
 * 
 * @author zqy
 */
public class Calculator {
	// ***********************从传感器获得的数据***********************//
	private static float x = 5;// 传感器的x值 (单位：°)
	private static float y = 5;// 传感器的y值
	private static float m = 30;// 人为设置的塔高(单位：米)
	private static float x_east_offset_angle;// 传感器初始放置时，x轴与正东方的夹角
	private static float adjust_deflection_offset_interval;// 垂度矫正间隔时间

	// ***********************计算的数据***********************//
	private static float adjust_vertical_angle;// 常态下的垂度角
	private static float adjust_offset_distance;// 常态下的顶部偏移距离

	private static float vertical_angle;// 实时计算出的与竖直方向的偏向角
	private static float offset_distance;// 实时计算出的顶部偏移距离
	private static String offset_orientation;// 实时计算出的偏移方向（例：东偏南70°）
	
	private static List<Float> vertical_angle_List;

	public Calculator() {
		// TODO 每计算一次就实例化一个对象，构造函数自动更新值
		x = Constants.x;
		y = Constants.y;
		m = Constants.m;
		x_east_offset_angle = Constants.x_east_offset_angle;
		adjust_deflection_offset_interval = Constants.adjust_deflection_offset_interval;
		/*
		 * adjust_vertical_angle=Constants.adjust_vertical_angle;
		 * adjust_offset_distance=Constants.adjust_offset_distance;
		 * vertical_angle=Constants.vertical_angle;
		 * offset_distance=Constants.offset_distance;
		 * offset_orientation=Constants.offset_orientation;
		 */
		start();
	}

	/**
	 * 从这里开始 串口获得数据后，实例化一个Calculator对象即可，其他不管了
	 * 
	 * @author zqy
	 */
	public void start() {
		// System.out.println("竖直偏移角度:\t\t" + cal_Vertical_Angle() + "(°)");
		// System.out.println("顶部偏移距离:\t" + cal_Offset_Distance() + "(m)");
		// System.out.println("偏移方向：\t\t"+cal_Offset_Orientation());
		cal_Vertical_Angle();
		cal_Offset_Distance();
		cal_Offset_Orientation();
		cal_deflection();
		//以下部分将当前计算结果暂存Constant，方便后期的数据库功能扩展
		Constants.vertical_angle=vertical_angle;
		Constants.offset_distance=offset_distance;
		Constants.offset_orientation=offset_orientation;

	}

	/**
	 * 计算vertical_angle
	 * 
	 * @author zqy
	 */
	public static float cal_Vertical_Angle() {
		// 将x和y转成幅度值
		double y_rad = Math.toRadians(y);
		double x_rad = Math.toRadians(x);
		vertical_angle = (float) Math
				.toDegrees(Math.atan(Math.sqrt(Math.pow(Math.tan(x_rad), 2) + Math.pow(Math.tan(y_rad), 2))));
		String str=new String(vertical_angle+"");
		str=str.substring(0,4);
		View.devField.setText(str+"°");
		return vertical_angle;
	}

	/**
	 * 计算offset_distance
	 * 
	 * @author zqy
	 */
	public static float cal_Offset_Distance() {
		// 计算过程
		double y_rad = Math.toRadians(y);
		double x_rad = Math.toRadians(x);
		float result_angle_radians = (float) Math
				.atan(Math.sqrt(Math.pow(Math.tan(x_rad), 2) + Math.pow(Math.tan(y_rad), 2)));
		offset_distance = (float) ((float) m * Math.sin(result_angle_radians));
		String str=String.format("%.3f",offset_distance);
		System.out.println(str);
		//  Math.round()
		View.devdisField.setText(str+"m");
		return offset_distance;
	}

	/**
	 * 计算offset_orientation
	 * 
	 * @author zqy
	 */
	public static String cal_Offset_Orientation() {
		float temp_angle=0;
		offset_orientation = null;
		float X = x, Y = y;

		// 在这里之前还需要额外添加判断认为不倾斜（小范围的变化内）
		if (X == 0 && Y == 0) {
			// 未倾斜
			offset_orientation="未倾斜";
		}
		if (X <= 0 && Y >= 0) {
			// 向东北方向倾斜
			offset_orientation = "东偏北";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X <= 0 && Y <= 0) {
			// 向东南方向偏斜
			offset_orientation = "东偏南";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X >= 0 && Y >= 0) {
			// 向西北方向偏斜
			offset_orientation = "西偏北";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X >= 0 && Y <= 0) {
			// 向西南方向偏斜
			offset_orientation = "西偏南";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		offset_orientation=offset_orientation.substring(0, 7)+"°";
		View.devdirectiontextField.setText(offset_orientation);
		return offset_orientation;
	}
	
	/**
	 * 计算垂度相关数据
	 * @author zqy
	 */
	public static void cal_deflection(){
		int count=5;//用于一次计算的数据量（个数）
		vertical_angle_List=new ArrayList<Float>();
		if (vertical_angle_List.size()<count) {
			vertical_angle_List.add(vertical_angle);
		}else if (vertical_angle_List.size()==count) {
			
		}
	}
	
	/**
	 * 求一个存有float数据的List的平均值
	 * @author zqy
	 * @param list
	 * @return
	 */
	public static float getAvg(ArrayList<Float> list){
		float temp_sum=0;
		float temp_avg=0;
		Iterator it=list.iterator();
		while(it.hasNext()){
			temp_sum=temp_sum+(float)it.next();
		}
		temp_avg=temp_sum/list.size();
		return temp_avg;
	}
	
	/**
	 * 在有了平均数的前提下求方差
	 * @author zqy
	 */
	public float getVariance (ArrayList<Float> list, float avg) {
		float temp_variance=0;
		Iterator it=list.iterator();
		while(it.hasNext()){
			temp_variance=(float) (temp_variance+Math.pow(((float)it.next()-avg), 2));
		}
		return temp_variance/list.size();
	}

}
