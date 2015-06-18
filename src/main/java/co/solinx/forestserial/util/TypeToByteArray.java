package co.solinx.forestserial.util;

/**
 * Created by linx on 2015/6/17.
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
        for (int i = 0; i < f.length; i++) {
            f[i] = new Integer(l).byteValue();
            l = l >> 8;
        }
        return f;

    }

    /*
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

    public static void main(String[] args) {

        System.out.println((65534 >> 24));
        System.out.println((65534 >> 16));
        System.out.println((65534 >> 8));
        System.out.println(65534);
        System.out.println((65534 >> 24) & 0xff);
        System.out.println((65534 >> 16) & 0xff);
        System.out.println((65534 >> 8) & 0xff);
        System.out.println(65534 & 0xff);

        byte[] intByte= intToByteArr(0x01020304);
//		System.out.println(intByte);
//		System.out.println(hBytesToInt(intByte));

        System.out.println(0x01020304);

        String temp="01020304";
        temp.getBytes();
        byte[] intByteTemp={0x04,0x03 ,0x02,0x01};
        System.out.println("byte[]="+hBytesToInt(intByteTemp));
        System.out.println("byte[]="+StringUtil.bytesToString(intByte));

        byte[] tempf= intToByteArr(65534);
        System.out.println("byte[]="+StringUtil.bytesToString(tempf));
    }

}
