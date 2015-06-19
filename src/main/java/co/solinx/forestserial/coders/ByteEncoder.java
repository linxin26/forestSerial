package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Request;
import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder {


    public static void main(String[] args) {

        Test request = new Test();
        request.setA(10);

        ByteEncoder encoder = new ByteEncoder();
        encoder.encoder(request);

    }

    public byte[] encoder(Object obj) {
        FieldUtil fieldUtil=new FieldUtil();
        byte[] byteData=new byte[100];
        byteData[0]=88;

        String className = this.getClassName(obj);
        byte[] clazz= className.getBytes();
        //类长度
        byteData[1]= (byte) clazz.length;
       System.arraycopy(clazz,0,byteData,2,clazz.length);
        Field[] fieldArray = obj.getClass().getDeclaredFields();
        fieldArray= fieldUtil.fieldSort(fieldArray);

        for (Field field: fieldArray){

               System.out.println( field.getType().isPrimitive());
        }

        System.out.println(StringUtil.bytesToString(byteData));
       System.out.println(fieldUtil.getFieldValue(fieldArray[0], obj));

        System.out.println(className);

        return null;
    }



    public String getClassName(Object obj) {
        String className = obj.getClass().getName();

        return className;
    }


}
