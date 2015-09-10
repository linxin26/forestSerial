package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.coders.JSONEncoder;
import co.solinx.forestserial.util.FieldUtil;

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

    }

    @Override
    public void writePrimitiveField(Field[] fields, Object obj) throws Exception {
        for (Field field:fields){
            field.setAccessible(true);
            Type type=field.getType();
            if(Integer.TYPE==type){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }
        }
    }

    @Override
    public void writeObjectFields(Field[] fields, Object obj) throws Exception {
        for (Field field:fields){
            field.setAccessible(true);
            Object value=field.get(obj);
        }
    }

    @Override
    public void writeTag(byte tag) {

    }

    @Override
    public void writeObjectField(Field field, Object value, Class typeName) {

    }

    @Override
    public void writeArray(Field field, Object value) {

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

