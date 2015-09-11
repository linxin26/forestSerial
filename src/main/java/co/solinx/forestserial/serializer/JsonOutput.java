package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.coders.JSONEncoder;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by linx on 2015/9/8.
 */
public class JsonOutput implements Output{


    Encoder encoder;

    @Override
    public void writeObject(Object obj) throws Exception {

        this.writeObjectHeader(obj.getClass());
        this.initClassInfo(obj);
        this.writeField(obj);
    }

    @Override
    public void writeField(Object obj) throws Exception {

        Field[] fields=obj.getClass().getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField=fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);

        this.writePrimitiveField(primitiveField, obj);
        this.writeObjectFields(objectField, obj);
    }

    @Override
    public void writeObject(Object obj, Class clazz) {

    }

    @Override
    public void writeSuperClass(Object obj, Class superClass) {

    }

    @Override
    public void writeObjectHeader(Class clazz) {
        encoder.writeClass(clazz);
    }

    @Override
    public void writePrimitiveField(Field[] fields, Object obj) throws Exception {
        for (Field field:fields){
            field.setAccessible(true);
            Type type=field.getType();
            if(Integer.TYPE==type){
                int value=field.getInt(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeInt(value);
            }else if(Long.TYPE==type){
                long value=field.getLong(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeLong(value);
            }else if(Short.TYPE==type){
                short value=field.getShort(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeShort(value);
            }else if(Byte.TYPE==type){
                byte value=field.getByte(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeByte(value);
            }else if(Character.TYPE==type){
                String value=StringUtil.convert(String.valueOf(field.getChar(obj)));
                encoder.writeFieldName(field.getName());
                encoder.writeString(value);
                encoder.writeSymbol(",");
            }else if(Float.TYPE==type){
                float value=field.getFloat(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeFloat(value);
            }else if(Double.TYPE==type){
                double value=field.getDouble(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeDouble(value);
            }else if(Boolean.TYPE==type){
                boolean value=field.getBoolean(obj);
                encoder.writeFieldName(field.getName());
                encoder.writeBoolean(value);
            }
        }
    }

    @Override
    public void writeObjectFields(Field[] fields, Object obj) throws Exception {
        for (Field field:fields){
            field.setAccessible(true);
            Object value=field.get(obj);
            if(value!=null) {
                this.writeObjectField(field, value, field.getType());
            }
        }
    }

    @Override
    public void writeTag(byte tag) {

    }

    @Override
    public void writeObjectField(Field field, Object value, Class typeName) {

        if(String.class==typeName){
            encoder.writeFieldName(field.getName());
            encoder.writeString((String) value);
        }else if(Integer.class==typeName){
            encoder.writeFieldName(field.getName());
            encoder.writeInt((Integer) value);
        }else if(typeName.isArray()){
            this.writeArray(field,value);
        }


    }

    @Override
    public void writeArray(Field field, Object value) {
        int len= Array.getLength(value);
        Class componentType=value.getClass().getComponentType();
        if(!componentType.isArray()){
            if(encoder.isPrimitiveArray(componentType)){
                encoder.writeFieldName(field.getName());
                encoder.writePrimitiveArray(value,len);
            }else{
                Object[] arr = (Object[]) value;
                encoder.writeFieldName(field.getName());
                encoder.writeSymbol("[");
                for (int i = 0; i < len; i++) {
//                    Object write = arr[i];
                    this.writeObjectArray(arr[i], arr[0].getClass());
                    if(i!=len-1){
                        encoder.writeSymbol(",");
                    }
                }
                encoder.writeSymbol("]");
                encoder.writeSymbol(",");
            }
        }
    }

    public void writeObjectArray(Object value,Class type){
        if(String.class==type){
            encoder.writeString((String) value);
        }
    }

    @Override
    public void setEncoder(Encoder encoder) {
        this.encoder=encoder;
    }

    @Override
    public String toJsonString() {
        return encoder.toJsonString();
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public void initClassInfo(Object clazz) {

    }
}

