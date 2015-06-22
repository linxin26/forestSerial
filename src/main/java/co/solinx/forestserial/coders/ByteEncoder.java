package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder {


    public static void main(String[] args) {

        Test request = new Test();
        request.setA(10);
        request.setB(5);
        request.setCc(254);
        request.setBl(true);
        request.setBt((byte) 12);
        request.setCr('A');
        request.setSt((short) 27);
        request.setFl(12.3f);
        request.setDl(13.2);

        ByteEncoder encoder = new ByteEncoder();
        encoder.encoder(request);

    }

    public byte[] encoder(Object obj) {
        FieldUtil fieldUtil = new FieldUtil();
        byte[] byteData = new byte[100];
        byteData[0] = 88;

        String className = this.getClassName(obj);
        byte[] clazz = className.getBytes();
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

        this.primitiveTypeToByte(primitiveFields, obj);
//        for (Field field: fieldArray){
//
//               System.out.println( field.getType().isPrimitive());
//        }

        System.out.println(StringUtil.bytesToString(byteData));
        System.out.println(fieldUtil.getFieldValue(fieldArray[0], obj));

        System.out.println(className);

        return null;
    }

    public byte[] objectFieldTypeToByte(Field[] fields, Object obj) {

        return new byte[0];
    }

    /**
     * 原始类型转换为byte数组
     *
     * @param fields
     * @param obj
     * @return
     */
    public byte[] primitiveTypeToByte(Field[] fields, Object obj) {
        byte[] primitiveByte = new byte[fields.length * 4];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String typeName=field.getType().getName();
            field.setAccessible(true);
            byte[] fieldByte;
            try {

                if (typeName.equals("int")) {
                    int value = field.getInt(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                } else if (typeName.equals("long")) {
                    int value = (int) field.getLong(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                } else if(typeName.equals("boolean")){
                    boolean value=field.getBoolean(obj);
                    if(value){
                        fieldByte=TypeToByteArray.intToByteArr(1);
                    }else{
                        fieldByte=TypeToByteArray.intToByteArr(0);
                    }
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                }else if(typeName.equals("char")){
                    char value= field.getChar(obj);
                    fieldByte=TypeToByteArray.intToByteArr(value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                }else if(typeName.equals("float")){
                    float value=field.getFloat(obj);
                    fieldByte= TypeToByteArray.intToByteArr((int) value);
                    System.arraycopy(fieldByte,0,primitiveByte,i*4,fieldByte.length);
                }else if(typeName.equals("double")){
                    double value=field.getDouble(obj);
                    fieldByte=TypeToByteArray.intToByteArr((int) value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                }else if(typeName.equals("short")){
                    short value=field.getShort(obj);
                    fieldByte=TypeToByteArray.intToByteArr(value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                }else if(typeName.equals("byte")){
                    byte value=field.getByte(obj);
                    fieldByte=TypeToByteArray.intToByteArr(value);
                    System.arraycopy(fieldByte, 0, primitiveByte, i * 4, fieldByte.length);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println(StringUtil.bytesToString(primitiveByte));
        return primitiveByte;
    }


    public String getClassName(Object obj) {
        String className = obj.getClass().getName();

        return className;
    }


}
