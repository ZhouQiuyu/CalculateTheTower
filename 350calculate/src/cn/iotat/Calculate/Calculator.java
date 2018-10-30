package cn.iotat.Calculate;

import cn.iotat.Constants.Constants;
import cn.iotat.SerialPort.SerialPort;
import cn.iotat.View.View;
import gnu.io.PortInUseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ���еļ��㹦��������ʵ��
 * 
 * @author zqy
 */
public class Calculator {
	// ***********************�Ӵ�������õ�����***********************//
	private static float x = 5;// ��������xֵ (��λ����)
	private static float y = 5;// ��������yֵ
	private static float m = 30;// ��Ϊ���õ�����(��λ����)
	private static float x_east_offset_angle;// ��������ʼ����ʱ��x�����������ļн�
	private static float adjust_deflection_offset_interval;// ���Ƚ������ʱ��

	// ***********************���������***********************//
	private static float adjust_vertical_angle;// ��̬�µĴ��Ƚ�
	private static float adjust_offset_distance;// ��̬�µĶ���ƫ�ƾ���

	private static float vertical_angle;// ʵʱ�����������ֱ�����ƫ���
	private static float offset_distance;// ʵʱ������Ķ���ƫ�ƾ���
	private static String offset_orientation;// ʵʱ�������ƫ�Ʒ���������ƫ��70�㣩
	
	private static List<Float> vertical_angle_List;

	public Calculator() {
		// TODO ÿ����һ�ξ�ʵ����һ�����󣬹��캯���Զ�����ֵ
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
	 * �����￪ʼ ���ڻ�����ݺ�ʵ����һ��Calculator���󼴿ɣ�����������
	 * 
	 * @author zqy
	 */
	public void start() {
		// System.out.println("��ֱƫ�ƽǶ�:\t\t" + cal_Vertical_Angle() + "(��)");
		// System.out.println("����ƫ�ƾ���:\t" + cal_Offset_Distance() + "(m)");
		// System.out.println("ƫ�Ʒ���\t\t"+cal_Offset_Orientation());
		cal_Vertical_Angle();
		cal_Offset_Distance();
		cal_Offset_Orientation();
		cal_deflection();
		//���²��ֽ���ǰ�������ݴ�Constant��������ڵ����ݿ⹦����չ
		Constants.vertical_angle=vertical_angle;
		Constants.offset_distance=offset_distance;
		Constants.offset_orientation=offset_orientation;

	}

	/**
	 * ����vertical_angle
	 * 
	 * @author zqy
	 */
	public static float cal_Vertical_Angle() {
		// ��x��yת�ɷ���ֵ
		double y_rad = Math.toRadians(y);
		double x_rad = Math.toRadians(x);
		vertical_angle = (float) Math
				.toDegrees(Math.atan(Math.sqrt(Math.pow(Math.tan(x_rad), 2) + Math.pow(Math.tan(y_rad), 2))));
		String str=new String(vertical_angle+"");
		str=str.substring(0,4);
		View.devField.setText(str+"��");
		return vertical_angle;
	}

	/**
	 * ����offset_distance
	 * 
	 * @author zqy
	 */
	public static float cal_Offset_Distance() {
		// �������
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
	 * ����offset_orientation
	 * 
	 * @author zqy
	 */
	public static String cal_Offset_Orientation() {
		float temp_angle=0;
		offset_orientation = null;
		float X = x, Y = y;

		// ������֮ǰ����Ҫ��������ж���Ϊ����б��С��Χ�ı仯�ڣ�
		if (X == 0 && Y == 0) {
			// δ��б
			offset_orientation="δ��б";
		}
		if (X <= 0 && Y >= 0) {
			// �򶫱�������б
			offset_orientation = "��ƫ��";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X <= 0 && Y <= 0) {
			// ���Ϸ���ƫб
			offset_orientation = "��ƫ��";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X >= 0 && Y >= 0) {
			// ����������ƫб
			offset_orientation = "��ƫ��";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		if (X >= 0 && Y <= 0) {
			// �����Ϸ���ƫб
			offset_orientation = "��ƫ��";
			temp_angle=(float) Math.abs(Math.toDegrees(Math.atan((Math.tan(Math.toRadians(Y)))/(Math.tan(Math.toRadians(X))))));
			offset_orientation+=(temp_angle);
		}
		offset_orientation=offset_orientation.substring(0, 7)+"��";
		View.devdirectiontextField.setText(offset_orientation);
		return offset_orientation;
	}
	
	/**
	 * ���㴹���������
	 * @author zqy
	 */
	public static void cal_deflection(){
		int count=5;//����һ�μ������������������
		vertical_angle_List=new ArrayList<Float>();
		if (vertical_angle_List.size()<count) {
			vertical_angle_List.add(vertical_angle);
		}else if (vertical_angle_List.size()==count) {
			
		}
	}
	
	/**
	 * ��һ������float���ݵ�List��ƽ��ֵ
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
	 * ������ƽ������ǰ�����󷽲�
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
