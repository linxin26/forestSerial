package co.solinx.forestserial.coders;

import co.solinx.forestserial.serializer.ClassInfo;

/**
 * Created by linx on 2015-06-22.
 */
public class ByteDecoder implements Decoder {

    public Object decoder(byte[] byteData) throws Exception {


        System.out.println("-------------------------------decoder-------------------------");

        ClassInfo classInfo = new ClassInfo(byteData);
        Object clazz = classInfo.getInstanceObject();
        //解码字段
        classInfo.deField();

        //解码父类
        classInfo.superClassDeCode(clazz, clazz.getClass().getSuperclass());

        return clazz;
    }
}
