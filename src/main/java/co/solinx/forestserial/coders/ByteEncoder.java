package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Response;
import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.serializer.ClassInfo;
import co.solinx.forestserial.util.StringUtil;

import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder implements Encoder{


    public static void main(String[] args) throws Exception {
        Test request = new Test();
        request.setA(10);
        request.setB(5);
        request.setCc(254);
        request.setBl(false);
        request.setBt((byte) 12);
        request.setCr('A');
        request.setSt((short) 27);
        request.setFl(12.3f);
        request.setDl(13.2);
        request.setBa("Ba");
        request.setBb("Bb");
        request.setInte(132);
        request.setLongNum(345l);
        request.setCharNum('B');
        request.setBooleanNum(true);
        request.setFloatNum(19.5f);
        request.setDoubleNum(20.1d);
        request.setShortNum((short) 21);
        request.setByteNum((byte) 29);
        request.setStringString("Stringing");
        request.setZzzz(9999999l);
        request.setObj(12);

        Response response = new Response();
        response.setResult("类字段");
        response.setSn(998);
        response.setBaseID(188);

        request.setResponse(response);
        request.setBaseClassID(190000);
        request.setResult("result");
        request.setSuperSn(18880);
        request.setBaseID(199);

        ByteEncoder encoder = new ByteEncoder();
        ByteDecoder decoder = new ByteDecoder();
        byte[] dataByte = encoder.encoder(request);
        Test temp = (Test) decoder.decoder(dataByte);

        System.out.println(temp.toString());
    }

    /**
     * 对象转码为Byte[]
     *
     * @param obj 对象
     * @return 返回编码后的byte数组
     */
    public byte[] encoder(Object obj) {
        /**
         * Byte[]=标志+类名长度+原始类型+引用类型+父类
         * 每个引用类型
         */


        ClassInfo classInfo=new ClassInfo(obj);

        byte[] classNameByte = classInfo.getClassNameByte();

        //父类
        Class superClass = obj.getClass().getSuperclass();
        //父类
        byte[] superByte = classInfo.superClassToByte(obj, superClass);

        byte[] fieldByte = classInfo.fieldBytes( );


        System.out.println("类名：" + StringUtil.bytesToString(classNameByte));

        ByteBuffer fieldBuf = ByteBuffer.allocate(fieldByte.length + superByte.length + classNameByte.length);
        fieldBuf.put(classNameByte);
        fieldBuf.put(fieldByte);
        fieldBuf.put(superByte);
        System.out.println("合并后：" + StringUtil.bytesToString(fieldBuf.array()));
        return fieldBuf.array();
    }

}
