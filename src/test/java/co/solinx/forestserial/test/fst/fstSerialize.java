package co.solinx.forestserial.test.fst;

import co.solinx.forestserial.test.Request;
import co.solinx.forestserial.test.Test;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;
import org.nustaq.serialization.FSTConfiguration;

/**
 * Created by linx on 2015/6/19.
 */
public class fstSerialize {

    /**
     * 00 01 20 63 6f 2e 73 6f 6c 69 6e 78 2e 66 6f 72 65 73 74 73 65 72 69 61 6c 2e 74 65 73 74 2e 54 65 73 74 0a 0a f7 78 fc 02 31 32 fc 02 61 31 fc 01 62 00
     *
     * �೤�ȣ�20
     *  0a 0a
     * @param args
     */
    public static void main(String[] args) {
        Request request=new Request();
//        request.setId(123);
        request.setSn(21);

        FSTConfiguration conf=FSTConfiguration.createDefaultConfiguration();


        Test test=new Test();
//       test.setR(request);
        byte[] datas= conf.asByteArray(test);

        System.out.println(StringUtil.bytesToString(datas));
        System.out.println(new String(datas));
        System.out.println( conf.asObject(datas).toString());

        System.out.println(StringUtil.bytesToString(fstSerialize.getBytes(10.1f)));
       System.out.println(StringUtil.bytesToString(fstSerialize.getBytes(12.1d)));
    }

    public static byte[] getBytes(int data)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(float data)
    {
        int intBits = Float.floatToIntBits(data);
        System.out.println(intBits);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data){
        long longBits=Double.doubleToLongBits(data);
        System.out.println("longBits "+longBits);
        byte[] dataByte= TypeToByteArray.longToByteArr(longBits);
        return dataByte;
    }

}
