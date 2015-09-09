package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;

/**
 * Created by linx on 2015/9/8.
 */
public class JsonOutput implements Output{


    @Override
    public void writeObject(Object obj) {

    }

    @Override
    public void writeField(Object obj) {

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
    public void writePrimitiveField(Field[] fields, Object obj) {

    }

    @Override
    public void writeObjectFields(Field[] fields, Object obj) {

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

    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public void initClassInfo(Object clazz) {

    }
}

