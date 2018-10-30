package cn.iotat.Constants;
/**
 * 用于保存所有数值
 * 三个部分的数据交互通过这里进行
 * @author Qiuyu
 *
 */
public class Constants {
	//***********************从传感器获得的数据***********************//
	
	public static float x;//传感器的x值
	public static float y;//传感器的y值
	
	//人为设置
	public static float m;//人为设置得塔高
	public static float x_east_offset_angle;//传感器初始放置时，x轴与正东方的夹角(顺时针为正)
	public static float adjust_deflection_offset_interval;//垂度矫正间隔时间
	
	
	//***********************计算的数据***********************//
	
	public static float adjust_vertical_angle;//常态下的垂度角
	public static float adjust_offset_distance;//常态下的顶部偏移距离
	
	public static float vertical_angle;//实时计算出的与竖直方向的偏向角
	public static float offset_distance;//实时计算出的顶部偏移距离
	public static String offset_orientation;//实时计算出的偏移方向（例：东偏南70°）
	
}
