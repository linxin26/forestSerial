package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder {


    public static void main(String[] args) {

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

        ByteEncoder encoder = new ByteEncoder();
        ByteDecoder decoder=new ByteDecoder();
        byte[] dataByte= encoder.encoder(request);
        Test temp= (Test) decoder.decoder(dataByte);

        System.out.println(temp.toString());
    }

    public byte[] encoder(Object obj) {
        FieldUtil fieldUtil = new FieldUtil();

        String className = this.getClassName(obj);
        byte[] clazz = className.getBytes();
        byte[] byteData = new byte[clazz.length+2];
        byteData[0] = 88;
        //类长度
        byteData[1] = (byte) clazz.length;
        //类全限定名
        System.arraycopy(clazz, 0, byteData, 2, clazz.length);
        //所有声明的字段字段
        Field[] fieldArray = obj.getClass().getDeclaredFields();


        //对字段按名称排序
        fieldArray = fieldUtil.fieldSort(fieldArray);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        byte[] primitiveByte= this.primitiveTypeToByte(primitiveFields, obj);
        byte[] objectByte= this.objectFieldTypeToByte(objectFields,obj);

        System.out.println(StringUtil.bytesToString(objectByte));
//        for (Field field: fieldArray){
//
//               System.out.println( field.getType().isPrimitive());
//        }

        System.out.println(StringUtil.bytesToString(byteData));
        System.out.println(fieldUtil.getFieldValue(fieldArray[0], obj));

        byte[] result=new byte[primitiveByte.length+byteData.length+objectByte.length];
        System.arraycopy(byteData,0,result,0,byteData.length);
        System.arraycopy(primitiveByte,0,result,byteData.length,primitiveByte.length);
        System.arraycopy(objectByte,0,result,primitiveByte.length+byteData.length,objectByte.length);

        System.out.println(className);
        System.out.println(StringUtil.bytesToString(result));

        return result;
    }

    public byte[] objectFieldTypeToByte(Field[] fields, Object obj) {
        byte[] byteData=new byte[0];
        for(Field field:fields){
            String typeName=field.getType().getSimpleName();
            field.setAccessible(true);
//            System.out.println(typeName);
            try {
                if (typeName.equals("String")) {
                    String value= (String) field.get(obj);
                    byte[] valueBytes=new byte[value.getBytes().length+2];
                    valueBytes[0]=0x0f;
                    valueBytes[1]= (byte) value.getBytes().length;
                    System.arraycopy(value.getBytes(),0,valueBytes,2,value.getBytes().length);

                    byte[] tempByte=new byte[byteData.length+valueBytes.length];
                    System.arraycopy(byteData,0,tempByte,0,byteData.length);
                    System.arraycopy(valueBytes,0,tempByte,byteData.length,valueBytes.length);
                    byteData=tempByte;
//                    System.out.println(value);
//                    System.out.println(StringUtil.bytesToString(byteData));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return byteData;
    }

    /**
     * 原始类型转换为byte数组
     *
     * @param fields
     * @param obj
     * @return
     */
    public byte[] primitiveTypeToByte(Field[] fields, Object obj) {
        ByteBuffer byteBuf= ByteBuffer.allocate(fields.length * 8);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String typeName=field.getType().getName();
            field.setAccessible(true);
            byte[] fieldByte;
            try {

                if (typeName.equals("int")) {
                    int value = field.getInt(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("long")) {
                    long value =  field.getLong(obj);
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if(typeName.equals("boolean")){
                    boolean value=field.getBoolean(obj);
                    if(value){
                        fieldByte=TypeToByteArray.intToByteArr(1);
                    }else{
                        fieldByte=TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("char")){
                    char value= field.getChar(obj);
                    fieldByte=TypeToByteArray.charToByteArr(value);
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("float")){
                    float value=field.getFloat(obj);
                    fieldByte= TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("double")){
                    double value=field.getDouble(obj);
                    fieldByte=TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("short")){
                    short value=field.getShort(obj);
                    fieldByte=TypeToByteArray.shortToByteArr(value);
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("byte")){
                    byte value=field.getByte(obj);
                    fieldByte=new byte[1];
                    fieldByte[0]=value;
                    byteBuf.put(fieldByte);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(StringUtil.bytesToString(primitiveByte));
        byte[] resultByte=new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(),0,resultByte,0,byteBuf.position());
        System.out.println(StringUtil.bytesToString(resultByte));
        return resultByte;
    }

    public void put(ByteBuffer byteBuf,byte[] bytes,int length){

        System.out.println("剩余："+byteBuf.remaining());
        if(byteBuf.remaining()<length){
            ByteBuffer tempBuf=ByteBuffer.allocate(byteBuf.array().length + 4);
            tempBuf.clear();
            tempBuf.put(byteBuf.array());
            tempBuf.put(bytes);
            byteBuf=tempBuf;
            System.out.println("扩容");
            System.out.println(StringUtil.bytesToString(bytes));
            System.out.println(StringUtil.bytesToString(tempBuf.array()));
        }else{
            byteBuf.put(bytes);
        }
        System.out.println(StringUtil.bytesToString(byteBuf.array()));
//        byteBuf.put(bytes);
    }


    public String getClassName(Object obj) {
        String className = obj.getClass().getName();

        return className;
    }


}
