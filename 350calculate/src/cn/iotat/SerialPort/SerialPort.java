package cn.iotat.SerialPort;

import cn.iotat.Calculate.Calculator;
import cn.iotat.Constants.Constants;
import cn.iotat.Utils.ArrayUtils;
import cn.iotat.Utils.ShowUtils;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class SerialPort {
    /**
     * 静态的串口对象，保存当前连接的串口
     */
    public static gnu.io.SerialPort theOnlyPort = null;

    /**
     * @Description: 获得串口名字
     * @Param: void
     * @return: portNames
     * @Author: pang
     * @date: 2018/10/28
     */
    public static ArrayList<String> getPorts() {
        Enumeration<CommPortIdentifier> portsList = CommPortIdentifier.getPortIdentifiers();
        //将得到的串口对象存放在这个枚举变量中，为什么不能是List
        ArrayList<String> portsNameList = new ArrayList<String>();//保存串口名字的List
        /*遍历枚举变量并将串口名字保存到List*/
        while (portsList.hasMoreElements()) {
            String str = portsList.nextElement().getName();
            portsNameList.add(str);
        }
        return portsNameList;
    }

    /**
     * @Description: 打开串口
     * @Param: protName, bps
     * @Throws: PortInUseException
     * @return: SerialPort
     * @Author: Pang
     * @Date: 2018/10/28
     */
    public static gnu.io.SerialPort openPort(String portName, int bps) throws PortInUseException {
        try {
            /*通过串口名返回串口实例*/
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            /*设置串口名和操作时间，并将成功的返回实例赋值给一个新的串口对象*/
            CommPort commPort = portIdentifier.open(portName, 2000);
            /*判断这个串口对象是不是串口类的子类*/
            if (commPort instanceof gnu.io.SerialPort) {
                /*强制转换为seriaPort*/
                gnu.io.SerialPort serialPort = (gnu.io.SerialPort) commPort;
                /*配置串口参数*/
                try {
                    /*按照先后循序依次为 波特率，数据位，停止位，校检位*/
                    serialPort.setSerialPortParams(bps, gnu.io.SerialPort.DATABITS_8, gnu.io.SerialPort.STOPBITS_1, gnu.io.SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
                return serialPort;//返回这个串口实例
            }
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        }
        return null;//如果串口对象不是串口类的子类，则返回一个空实例
    }

    /**
     * @Description: 关闭串口
     * @Param: serialPort
     * @return: void
     * @Author: Pang
     * @Date: 2018/10/28
     */
    public static void closePort(gnu.io.SerialPort serialPort) {
        //如果serialPort为空才关闭，不然会抛出空指针异常
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * @Description: 接受串口发来的数据
     * @Param: serialPort
     * @return: (byte[])data
     * @Author: Pang
     * @Date: 2018/10/28
     */
    private static int dataConter = 0;

    public static void readData(gnu.io.SerialPort serialPort) {
        InputStream in = null;
        byte[] data = {};
        try {
            in = serialPort.getInputStream();//获得输入流
            byte[] buffer = new byte[1];//传入一字节数据
            int bytesNum = in.read(buffer);//??????????
            while (bytesNum > 0) {
                data = ArrayUtils.concat(data, buffer);//这里是一个字符流操作的工具类静态方法，合并两个字符串
                bytesNum = in.read(buffer);
            }
        } catch (IOException e) {
            ShowUtils.errorMessage("无法读取数据");
            e.printStackTrace();
        }
        if (dataConter++ > 50) {//数据更新的频率可以通过这里修正，单位1为0.1秒
            float[] angels = ArrayUtils.toAngle(data);
            Constants.y = angels[0];
            Constants.x = angels[1];
            Calculator calculator=new Calculator();//*********
            dataConter=0;
        }
    }


    /**
     * @Description: 给串口添加监听器
     * @Param: SerialPort, DataAvailableListener
     * @return: void
     * @Author: Pang
     * @Date: 2018/10/28
     */
    public static void addListener(gnu.io.SerialPort serialPort, DataAvailableListener listener) {
        try {
            // 给串口添加监听器
            serialPort.addEventListener(new SerialPortListener(listener));
            // 设置当有数据到达时唤醒监听接收线程
            serialPort.notifyOnDataAvailable(true);
            // 设置当通信中断时唤醒中断线程
            serialPort.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            ShowUtils.errorMessage("监听串口时发生错误");
            e.printStackTrace();
        }
    }


    /**
     * @Description: 内部接口，接口方法用来写自己的接收数据逻辑
     * @Author: Pang
     * @Date: 2018/10/28
     */
    public interface DataAvailableListener {
        /**
         * 如果串口存在有效数据，则执行dataAvailable()方法
         */
        void dataAvailable();
    }


    /**
     * @program: SerialPortListener
     * @description: 内部静态类，继承接口SerialPortEventListener，实现监听接口
     * @author: Pang
     * @create: 2018-10-28 09：31
     **/
    public static class SerialPortListener implements SerialPortEventListener {
        private DataAvailableListener mDataAvailableListener;//内部接口的参数传递

        public SerialPortListener(DataAvailableListener mDataAvailableListener) {
            this.mDataAvailableListener = mDataAvailableListener;//获得接口的实例对象
        }

        /**
         * @param serialPortEvent
         * @description: 添加串口监听逻辑，主要使用到了SerialPortEvent.DATA_AVAILABLE
         */
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
                    if (mDataAvailableListener != null) {
                        //先假设当秒数为5的倍数的时候才执行接收操作
                        mDataAvailableListener.dataAvailable();
                    }
                    break;

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
                    break;

                case SerialPortEvent.CTS: // 3.清除待发送数据
                    break;

                case SerialPortEvent.DSR: // 4.待发送数据准备好了
                    break;

                case SerialPortEvent.RI: // 5.振铃指示
                    break;

                case SerialPortEvent.CD: // 6.载波检测
                    break;

                case SerialPortEvent.OE: // 7.溢位（溢出）错误
                    break;

                case SerialPortEvent.PE: // 8.奇偶校验错误
                    break;

                case SerialPortEvent.FE: // 9.帧错误
                    break;

                case SerialPortEvent.BI: // 10.通讯中断
                    ShowUtils.errorMessage("通讯中断");
                    break;

                default:
                    break;
            }
        }
    }
}
