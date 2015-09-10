package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;

/**
 * Created by lin8x_000 on 2015-09-09.
 */
public interface Output {


    void writeObject(Object obj) throws Exception;

    void writeField(Object obj) throws Exception;

    void writeObject(Object obj,Class clazz);

    void writeSuperClass(Object obj,Class superClass);

    void writeObjectHeader(Class clazz);

    void writePrimitiveField(Field[] fields,Object obj) throws Exception;

    void writeObjectFields(Field[] fields,Object obj) throws IllegalAccessException, Exception;

    void writeTag(byte tag);

     void writeObjectField(Field field, Object value, Class typeName) ;

     void writeArray(Field field, Object value);

    void setEncoder(Encoder encoder);

    String toJsonString();

    byte[] toBytes();

    void initClassInfo(Object clazz);
}
