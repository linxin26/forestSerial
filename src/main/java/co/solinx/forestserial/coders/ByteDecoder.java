package co.solinx.forestserial.coders;

import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;

/**
 * Created by linx on 2015-06-22.
 */
public class ByteDecoder {

    public Object decoder(byte[] byteData) {
        int flag = byteData[0];
        int claLength = byteData[1];
        byte[] claNameByte = new byte[claLength];
        System.arraycopy(byteData, 2, claNameByte, 0, claLength);
        String claName = new String(claNameByte);

        byte[] fieldByte=new byte[byteData.length-(claLength+2)];
        System.arraycopy(byteData,claLength+2,fieldByte,0,fieldByte.length);
        System.out.println(claName);

        Object clazz= this.getClazzInstance(claName);
        FieldUtil fieldUtil=new FieldUtil();
        Field[] fieldArray=fieldUtil.fieldSort(clazz.getClass().getDeclaredFields());
        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);

        this.dePrimitive(clazz,primitiveFields,fieldByte);

        System.out.println(clazz);
        return clazz;
    }

    public void dePrimitive(Object obj,Field[] fields,byte[]dataByte){
        int index=0;
        try {
        for (Field field:fields){
            System.out.println(field);
            field.setAccessible(true);
            String type= field.getType().getName();
            if(type.equals("int")) {
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                    field.setInt(obj, value);
                index=index+4;
            }else if(type.equals("long")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setLong(obj, value);
                index=index+4;
            }else if(type.equals("float")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setFloat(obj, value);
                index=index+4;
            }else if(type.equals("double")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setDouble(obj, value);
                index=index+4;
            }else if(type.equals("char")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setChar(obj, (char) value);
                index=index+4;
            }else if(type.equals("boolean")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                boolean result;
                if(value==1){
                    result=true;
                }else{
                    result=false;
                }
                field.setBoolean(obj,result);
                index=index+4;
            }else if(type.equals("short")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setShort(obj, (short) value);
                index=index+4;
            }else if(type.equals("byte")){
                byte[] iByte=new byte[4];
                System.arraycopy(dataByte,index,iByte,0,4);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setByte(obj, (byte) value);
                index=index+4;
            }else{
                index=index+4;
            }
        }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getClazzInstance(String clazzName) {

        Object instance = null;
        try {
            Class cla  = Class.forName(clazzName);
              instance= cla.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
