package co.solinx.forestserial.util;

/**
 * Created by linx on 2015/6/17.
 * 类型字符转换
 */
public class TypeToByteArray {


    /**
     * int转Byte数组
     *
     * @param param
     * @return
     */
    public static byte[] intToByteArr(int param) {
        byte[] arr = new byte[4];
        arr[0] = (byte) ((param >> 24) & 0xff);
        arr[1] = (byte) ((param >> 16) & 0xff);
        arr[2] = (byte) ((param >> 8) & 0xff);
        arr[3] = (byte) ((param & 0xff));
        return arr;
    }

    public static byte[] charToByteArr(char data) {
        byte[] bytes = new byte[2];
        bytes[1] = (byte) (data);
        bytes[0] = (byte) (data >> 8);
        return bytes;
    }

    /**
     * short转Byte数组
     *
     * @param param
     * @return
     */
    public static byte[] shortToByteArr(short param) {
        byte[] arr = new byte[2];
        arr[0] = (byte) ((param) >> 8 & 0xff);
        arr[1] = (byte) (param & 0xff);
        return arr;
    }

    /**
     * long转Byte数组
     *
     * @param param
     * @return
     */
    public static byte[] longToByteArr(long param) {
        byte[] arr = new byte[8];
        arr[0] = (byte) ((param >> 56) & 0xff);
        arr[1] = (byte) ((param >> 48) & 0xff);
        arr[2] = (byte) ((param >> 40) & 0xff);
        arr[3] = (byte) ((param >> 32) & 0xff);
        arr[4] = (byte) ((param >> 24) & 0xff);
        arr[5] = (byte) ((param >> 16) & 0xff);
        arr[6] = (byte) ((param >> 8) & 0xff);
        arr[7] = (byte) (param & 0xff);
        return arr;
    }

    /**
     * float转Byte数组
     *
     * @param param
     * @return
     */
    public static byte[] floatToByteArr(float param) {
        byte[] f = new byte[4];
        int l = Float.floatToIntBits(param);
        f = intToByteArr(l);
        return f;

    }

    public static byte[] doubleToByteArr(double param) {
        byte[] f;
        long d = Double.doubleToLongBits(param);
        f = longToByteArr(d);
        return f;
    }

    /**
     * byte[]转为double
     * @param b
     * @return
     */
    public static double getDouble(byte[] b) {
        long temp = ( (long) b[7] & 0xffl) | ( (long) b[6] << 8 & 0xff00l) | ( (long) b[5] << 16 & 0xff0000l) | ( (long) b[4] << 24 & 0xff000000l) | ( (long) b[3] << 32 & 0xff00000000l) |
                ( (long) b[2] << 40 & 0xff0000000000l) | ( (long) b[1] << 48 & 0xff000000000000l) | ( (long) b[0] << 56 & 0xff00000000000000l);
        return Double.longBitsToDouble(temp);
    }

    /**
     * byte[]转为float
     * @param b
     * @return
     */
    public static float getFloat(byte[] b) {
        int temp = (b[3] & 0xff) | (b[2] << 8 & 0xff00) | (b[1] << 16 & 0xff0000) | (b[0] << 24 & 0xff000000);
        return Float.intBitsToFloat(temp);
    }

    /**
     * byte[]转为short
     * @param b
     * @return
     */
    public static short hBytesToShort(byte[] b) {
        return (short) ((b[1] & 0xff) | (b[0] << 8 & 0xff00));
    }

    /**
     * 将高字节数组转换为int
     *
     * @param b byte[]
     *
     * @return int
     */
    public static int hBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] >= 0) {
                s = s + b[i];
            } else {
                s = s + 256 + b[i];
            }
            s = s * 256;
//            System.out.println("s=" + s);
//            System.out.println("b[" + i + "]=" + b[i]);
        }
        if (b[3] >= 0) {
            s = s + b[3];
//            System.out.println("s=" + s);
//            System.out.println("b[3]=" + b[3]);
        } else {
            s = s + 256 + b[3];
        }
        return s;
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[7]) | (0xff00L & ((long) bytes[6] << 8)) | (0xff0000L & ((long) bytes[5] << 16)) | (0xff000000L & ((long) bytes[4] << 24))
                | (0xff00000000L & ((long) bytes[3] << 32)) | (0xff0000000000L & ((long) bytes[2] << 40)) | (0xff000000000000L & ((long) bytes[1] << 48)) | (0xff00000000000000L & ((long) bytes[0] << 56));
    }

    public static void main(String[] args) {

        System.out.println((65534 >> 24));
        System.out.println((65534 >> 16));
        System.out.println((65534 >> 8));
        System.out.println(65534);
        System.out.println((65534 >> 24) & 0xff);
        System.out.println((65534 >> 16) & 0xff);
        System.out.println((65534 >> 8) & 0xff);
        System.out.println(65534 & 0xff);

        byte[] intByte = intToByteArr(0x01020304);
//		System.out.println(intByte);
//		System.out.println(hBytesToInt(intByte));

        System.out.println(0x01020304);

        String temp = "01020304";
        temp.getBytes();
        byte[] intByteTemp = {0x04, 0x03, 0x02, 0x01};
        System.out.println("byte[]=" + hBytesToInt(intByteTemp));
        System.out.println("byte[]=" + StringUtil.bytesToString(intByte));

        byte[] tempf = intToByteArr(65534);
        System.out.println("byte[]=" + StringUtil.bytesToString(tempf));
    }

}
