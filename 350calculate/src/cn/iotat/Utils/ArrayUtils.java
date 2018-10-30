package cn.iotat.Utils;

/**
 * @program: 350calculate
 * @description: 字符操作类
 * @author: Pang
 * @create: 2018-10-28 10:02
 **/
public class ArrayUtils {
    /**
     * 合并数组
     *
     * @param firstArray
     * @param secondArray
     * @return
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        /*如果两个数组有一个为空，就返回一个空实例*/
        if (firstArray == null || secondArray == null)
            return null;
        byte[] bytes = new byte[firstArray.length + secondArray.length];//新建一个临时数组为两个数组的长度之和
        /*合并数组*/
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length, secondArray.length);
        return bytes;
    }

    /**
     * bytes转化成字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString(b[i]));
        }
        return buffer.toString();
    }

    /**
     * byte转化成字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**
     * @Description: 获取数据位，表示为x角度和y的角度
     * @Param: bytes
     * @return: float[2], 其中float[0]为x角度，float[1]为y的角度
     * @Author: Pang
     * @Date: 2018/10/28
     */
    public static float[] toAngle(byte[] b) {
        String data = toHexString(b);
        int xAngle = Integer.parseInt(data.substring(6, 10), 16);
        int yAngle = Integer.parseInt(data.substring(10, 14), 16);
        float angels[] = new float[2];
        angels[0] = (float) (xAngle - 0x8000) / 1000;
        angels[1] = (float) (yAngle - 0x8000) / 1000;
        return angels;
    }
}
