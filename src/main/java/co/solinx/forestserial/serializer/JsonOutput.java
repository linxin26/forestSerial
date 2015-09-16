package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.coders.JSONEncoder;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        this.writeSuperClass(obj,obj.getClass().getSuperclass());
    }

    @Override
    public void writeObject(Object obj, Class clazz) throws Exception{
        Field[] fields = clazz.getDeclaredFields();
        FieldUtil fieldUtil = new FieldUtil();
        fields = fieldUtil.fieldSort(fields);
        Field[] primitiveField = fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField = fieldUtil.getObjectTypeField(fields);


        this.writePrimitiveField(primitiveField, obj);
        this.writeObjectFields(objectField, obj);
    }

    @Override
    public void writeSuperClass(Object obj, Class superClass) throws Exception{
        if(!"Object".equals(superClass.getSimpleName())) {
            this.writeSuperClass(obj,superClass.getSuperclass());
            this.writeObject(obj, superClass);
        }
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
            if(!field.getName().equals("serialVersionUID")) {
                encoder.writeFieldName(field.getName());
            }
            if(Integer.TYPE==type){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }else if(Long.TYPE==type){
                if(!field.getName().equals("serialVersionUID")) {
                long value=field.getLong(obj);
                encoder.writeLong(value);
                }
            }else if(Short.TYPE==type){
                short value=field.getShort(obj);
                encoder.writeShort(value);
            }else if(Byte.TYPE==type){
                byte value=field.getByte(obj);
                encoder.writeByte(value);
            }else if(Character.TYPE==type){
                String value=StringUtil.convert(String.valueOf(field.getChar(obj)));
                   if(!"\\u0000".equals(value.toString())){
                       value=String.valueOf(field.getChar(obj));
                   }
                encoder.writeString(value);
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

    @Override
    public void writeObjectFields(Field[] fields, Object obj) throws Exception {
        for (Field field:fields){
            field.setAccessible(true);
            Object value=field.get(obj);
            if(value!=null) {
                encoder.writeFieldName(field.getName());
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
            encoder.writeString((String) value);
        }else if(Integer.class==typeName){
            encoder.writeInt((Integer) value);
        }else if(typeName.isArray()){
            this.writeArray(field,value);
        }else if(Short.class==typeName){
            encoder.writeShort((Short) value);
        }else if(Long.class==typeName){
            encoder.writeLong((Long) value);
        }else if(Float.class==typeName){
            encoder.writeFloat((Float) value);
        }else if(Double.class==typeName){
            encoder.writeDouble((Double) value);
        }else if(Boolean.class==typeName){
            encoder.writeBoolean((Boolean) value);
        }else if(Character.class==typeName){
            encoder.writeChar((Character) value);
        }else if(Byte.class==typeName){
            encoder.writeByte((Byte) value);
        }else if(Map.class==typeName){
            this.writeMap(value);
        }else if(List.class==typeName){
            this.writeList(value);
        }
    }

    public void writeList(Object value){
        List list= (List) value;
        encoder.writeSymbol("{");
        for (Object temp:list){
            this.writeObjectField(null,temp,temp.getClass());
        }
        encoder.replaceLastSymbol(",",'\0');
        encoder.writeSymbol("}");
        encoder.writeSymbol(",");
    }


    public void writeMap(Object value){
        Map map= (Map) value;
        encoder.writeSymbol("{");
        for(Iterator iterator=map.entrySet().iterator();iterator.hasNext();){
            Map.Entry temp= (Map.Entry) iterator.next();
            encoder.writeFieldName(temp.getKey().toString());
            this.writeObjectField(null,temp.getValue(),temp.getValue().getClass());
        }
        encoder.replaceLastSymbol(",",'\0');
        encoder.writeSymbol("}");
        encoder.writeSymbol(",");
    }


    @Override
    public void writeArray(Field field, Object value) {
        int len= Array.getLength(value);
        Class componentType=value.getClass().getComponentType();
        if(!componentType.isArray()){
            if(encoder.isPrimitiveArray(componentType)){
                encoder.writePrimitiveArray(value,len);
            }else{
                Object[] arr = (Object[]) value;
                encoder.writeSymbol("[");
                for (int i = 0; i < len; i++) {
                    this.writeObjectArray(arr[i], arr[0].getClass());
                }
                encoder.replaceLastSymbol(",",'\0');
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

