package co.solinx.forestserial.coders;

import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

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
        Field[] objectFields=fieldUtil.getObjectTypeField(fieldArray);

        ByteBuffer byteBuf=ByteBuffer.wrap(fieldByte);
        this.dePrimitive(clazz,primitiveFields,byteBuf);
        this.deObject(clazz,objectFields,byteBuf);

        System.out.println(clazz);
        return clazz;
    }

    public void deObject(Object obj,Field[] fields,ByteBuffer byteBuf){
        for (Field field:fields){
            String typeName=field.getType().getSimpleName();
            field.setAccessible(true);
            try {
                if ("String".equals(typeName)) {
                    byteBuf.get();  //类型0f
                    int length = byteBuf.get();
                    byte[] valueByte = new byte[length];
                    byteBuf.get(valueByte);
                    field.set(obj, new String(valueByte));
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public void dePrimitive(Object obj,Field[] fields,ByteBuffer byteBuf){
        int index=0;
        try {
        for (Field field:fields){
            field.setAccessible(true);
            String type= field.getType().getName();
            if(type.equals("int")) {
                    field.setInt(obj, byteBuf.getInt());
                index=index+4;
            }else if(type.equals("long")){
                field.setLong(obj, byteBuf.getLong());
                index=index+8;
            }else if(type.equals("float")){
                byte[] iByte=new byte[4];
                byteBuf.get(iByte);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setFloat(obj,Float.intBitsToFloat( value));
                index=index+4;
            }else if(type.equals("double")){
                byte[] iByte=new byte[8];
                byteBuf.get(iByte);
                long value= TypeToByteArray.getLong(iByte);
                field.setDouble(obj, Double.longBitsToDouble(value));
                index=index+8;
            }else if(type.equals("char")){
                field.setChar(obj, byteBuf.getChar());
                index=index+2;
            }else if(type.equals("boolean")){
                byte[] iByte=new byte[4];
                byteBuf.get(iByte);
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
                field.setShort(obj, byteBuf.getShort());
                index=index+2;
            }else if(type.equals("byte")){
                field.setByte(obj, byteBuf.get());
                index=index+1;
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
