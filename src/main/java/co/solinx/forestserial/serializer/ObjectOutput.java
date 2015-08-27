package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.coders.JSONEncoder;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.util.FieldUtil;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by linx on 2015/7/22.
 */
public class ObjectOutput {

    Encoder encoder=new ByteEncoder();
    private SerializeContext serializeContext=new SerializeContext();


    public ObjectOutput(){

    }

    public void writeObject(Object obj) throws Exception {
        this.writeObjectHeader(obj.getClass());
        this.initClassInfo(obj);

        this.writeField(obj);

    }

   public void writeField(Object obj) throws Exception {

       Field[] fields=obj.getClass().getDeclaredFields();
       FieldUtil fieldUtil=new FieldUtil();
       fields=fieldUtil.fieldSort(fields);
       Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
       Field[] objectField=fieldUtil.getObjectTypeField(fields);

           this.writePrimitiveField(primitiveField, obj);
           this.writeObjectFields(objectField, obj);
           writeSuperClass(obj, obj.getClass().getSuperclass());
   }

    public void writeObject(Object obj ,Class clazz) throws Exception {
        this.writeObjectHeader(clazz);
        this.initClassInfo(obj);

        Field[] fields=clazz.getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);


            this.writePrimitiveField(primitiveField, obj);
            this.writeObjectFields(objectField, obj);

    }

    public void writeSuperClass(Object obj,Class superClass) throws Exception {
//        System.out.println("class ：" + obj.getClass().getName());
//        System.out.println("superClass ：" + superClass.getName());
        if(!"Object".equals(superClass.getSimpleName())) {
            writeSuperClass(obj,superClass.getSuperclass());
            writeObject(obj, superClass);
        }
//        else {
//            System.out.println("exit ：" + superClass.getName());
//        }
    }

    public void writeObjectHeader(Class clazz){
        encoder.writeTag(DataType.CLASS_NAME);
        encoder.writeClass(clazz);
    }

    /**
     * 基本类型字段
     * @param fields
     * @param obj
     * @throws IllegalAccessException
     */
    public void writePrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field:fields){
            field.setAccessible(true);
            Type type=field.getType();
            if(encoder instanceof JSONEncoder){
                encoder.writeString(field.getName());
            }
            if(Integer.TYPE==type){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }else if(Long.TYPE==type){
                 long value=field.getLong(obj);
                 encoder.writeLong(value);
            }else if(Byte.TYPE==type){
                byte value=field.getByte(obj);
                encoder.writeByte(value);
            }else if(Short.TYPE==type){
                short value=field.getShort(obj);
                encoder.writeShort(value);
            }else if(Character.TYPE==type){
                char value=field.getChar(obj);
                encoder.writeChar(value);
            }else if(Float.TYPE==type){
                float value=field.getFloat(obj);
                encoder.writeFloat(value);
            }else if(Double.TYPE==type){
                double value=field.getDouble(obj);
                encoder.writeDouble(value);
            }else if(Boolean.TYPE==type){
                boolean value=field.getBoolean(obj);
                encoder.writeBoolean(value);
            }
        }
    }

    public void writeObjectFields(Field[] fields,Object obj) throws Exception {
        if(fields.length>0 && obj!=null){
            for (Field field: fields){
                field.setAccessible(true);
                Object value=field.get(obj);
                if(encoder instanceof JSONEncoder){
                    encoder.writeString(field.getName());
                }
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    this.writeObjectField(field, value, field.getType());
                }else{
                    encoder.writeByte((byte) 0);
                }
            }
        }
    }

    public void writeTag(byte tag){
        encoder.writeByte(tag);
    }

    /**
     * 对象类型字段
     * @param field
     * @param value
     * @param typeName
     * @throws Exception
     */
    public void writeObjectField(Field field,Object value,Class typeName) throws Exception {
            if(Integer.class==typeName){
                   writeTag(DataType.INTEGER);
                    encoder.writeInt((Integer) value);
            }else if(Short.class==typeName){
                    writeTag(DataType.SHORT);
                    encoder.writeShort((Short) value);
            }else if(Byte.class==typeName){
                    writeTag(DataType.BYTE);
                    encoder.writeByte((Byte) value);
            }else if(Long.class==typeName){
                    writeTag(DataType.LONG);
                   encoder.writeLong((Long) value);
            }else if(ArrayList.class==typeName|| List.class==typeName){
                    writeTag(DataType.LIST);
                serializeContext.setSerializer(new ArrayListSerializer());
                serializeContext.writeObject(this, field, value, encoder);
            }else if(Character.class==typeName){
                  writeTag(DataType.CHAR);
                    encoder.writeChar((Character) value);
            }else if(Float.class==typeName){
                    writeTag(DataType.FLOAT);
                    encoder.writeFloat((Float) value);
            }else if(Double.class==typeName){
                   writeTag(DataType.DOUBLE);
                    encoder.writeDouble((Double) value);
            }else if(Boolean.class==typeName){
                    writeTag(DataType.BOOLEAN);
                    encoder.writeBoolean((Boolean) value);
            }else if(Object.class==typeName){
                    writeTag(DataType.OBJECT);
                    encoder.writeObject(value);
            }else if(String.class==typeName){
                   writeTag(DataType.STRING);
                    encoder.writeString((String) value);
            }else if(Map.class==typeName|| HashMap.class==typeName){
                   writeTag(DataType.MAP);
                serializeContext.setSerializer(new MapSerializer());
                serializeContext.writeObject(this, field, value, encoder);
            }else if(field.getType().isEnum()){
                writeTag(DataType.ENUM);
                encoder.writeString(value.getClass().getName());
                encoder.writeInt(((Enum)value).ordinal());
            }else if(typeName.isArray()){
                writeTag(DataType.ARRAY);
                writeArray(field,value);
            }else{
                writeObject(value);
            }
    }

    /**
     * 数组
     * @param field
     * @param value
     * @throws Exception
     */
    public void writeArray(Field field, Object value) throws Exception {
        int len=Array.getLength(value);
        encoder.writeClass(value.getClass());
        encoder.writeInt(len);
        Class componentType=value.getClass().getComponentType();
        //非多维数组
        if(!componentType.isArray()) {
            if (encoder.isPrimitiveArray(componentType)) {  //基本类型数组
                encoder.writePrimitiveArray(value, len);
            } else {//对象数组
                Object[] arr = (Object[]) value;
                for (int i = 0; i < len; i++) {
                    Object write = arr[i];
                    writeObjectField(field, write, write.getClass());
                }
            }
        }else{
            Object[] arr = (Object[])value;
            for (int i = 0; i < len; i++) {
                writeArray(field,arr[i]);
            }
        }
    }


    public void initClassInfo(Object clazz){
    }

    public byte[] toBytes(){
        return encoder.toByte();
    }

    public String toJsonString(){
        return encoder.toJsonString();
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
