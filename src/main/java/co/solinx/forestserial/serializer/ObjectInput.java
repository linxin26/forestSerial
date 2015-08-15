package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.util.FieldUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by linx on 2015/7/23.
 */
public class ObjectInput {

    private ByteDecoder decoder;
    private SerializeContext serializeContext = new SerializeContext();

    public ObjectInput(byte[] bytes) {
        decoder = new ByteDecoder(bytes);
    }

    public Object readObject() throws Exception {
        String className = (String) decoder.readObject();
        Object clazz = Class.forName(className).newInstance();
        readFields(clazz);


        return clazz;
    }

    public void readObject(Object obj, Class cla) throws Exception {
        String className = (String) decoder.readObject();
        readFields(obj, cla);

    }

    public Object readFields(Object obj) throws Exception {

        Field[] fields = obj.getClass().getDeclaredFields();
        FieldUtil fieldUtil = new FieldUtil();
        fields = fieldUtil.fieldSort(fields);
        Field[] primitiveField = fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField = fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField, obj);
        this.readObjectFields(objectField, obj);

        readSuperClass(obj, obj.getClass().getSuperclass());

        return obj;
    }

    public Object readFields(Object obj, Class clazz) throws Exception {

        Field[] fields = clazz.getDeclaredFields();
        FieldUtil fieldUtil = new FieldUtil();
        fields = fieldUtil.fieldSort(fields);
        Field[] primitiveField = fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField = fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField, obj);
        this.readObjectFields(objectField, obj);


        return obj;
    }

    public void readSuperClass(Object obj, Class superClass) throws Exception {
        if (!"Object".equals(superClass.getSimpleName())) {
            readSuperClass(obj, superClass.getSuperclass());
            readObject(obj, superClass);

        }
    }


    public void readPrimitiveField(Field[] fields, Object obj) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            Type type = field.getType();
            if (Integer.TYPE == type) {
                field.set(obj, readInt());
            } else if (Long.TYPE == type) {
                field.set(obj, readLong());
            } else if (Short.TYPE == type) {
                field.set(obj, readShort());
            } else if (Byte.TYPE == type) {
                field.set(obj, readByte());
            } else if (Character.TYPE == type) {
                field.set(obj, readChar());
            } else if (Float.TYPE == type) {
                field.set(obj, readFloat());
            } else if (Double.TYPE == type) {
                field.set(obj, readDouble());
            } else if (Boolean.TYPE == type) {
                field.set(obj, readBoolean());
            }
        }
    }

    public boolean isNotNull() {
        boolean val = false;
        if (readByte() == 1) {
            val = true;
        }
        return val;
    }

    public void readObjectFields(Field[] fields, Object obj) throws Exception {
        for (Field field : fields) {
            field.setAccessible(true);
            readObjectField(field, obj);
        }
    }

    public Object instanceTagData() {
        Object value = null;
        byte tag = readByte();
        switch (tag) {
            case DataType.INTEGER:
                value = readInt();
                break;
            case DataType.SHORT:
                value = readShort();
                break;
            case DataType.BYTE:
                value = readByte();
                break;
            case DataType.STRING:
                value = readString();
                break;
            case DataType.FLOAT:
                value = readFloat();
                break;
            case DataType.DOUBLE:
                value = readDouble();
                break;
            case DataType.LONG:
                value = readLong();
                break;
            case DataType.BOOLEAN:
                value = readBoolean();
                break;
            case DataType.CHAR:
                value = readChar();
                break;
            case DataType.LIST:
                serializeContext.setSerializer(new ArrayListSerializer());
                value = serializeContext.instance(this);
                break;
            case DataType.MAP:
                MapSerializer mapSerializer = new MapSerializer();
                value = mapSerializer.instance(this);
                break;
        }
        return value;
    }

    /**
     * 对象
     * @param field
     * @param obj
     * @throws Exception
     */
    public void readObjectField(Field field, Object obj) throws Exception {
        Type type = field.getType();
        if (isNotNull()) {
            if (Integer.class == type && readByte() == DataType.INTEGER) {
                field.set(obj, readInt());
            } else if (Short.class == type && readByte() == DataType.SHORT) {
                field.set(obj, readShort());
            } else if (Long.class == type && readByte() == DataType.LONG) {
                field.set(obj, readLong());
            } else if (Byte.class == type && readByte() == DataType.BYTE) {
                field.set(obj, readByte());
            } else if (Character.class == type && readByte() == DataType.CHAR) {
                field.set(obj, readChar());
            } else if (Float.class == type && readByte() == DataType.FLOAT) {
                field.set(obj, readFloat());
            } else if (Double.class == type && readByte() == DataType.DOUBLE) {
                field.set(obj, readDouble());
            } else if (Boolean.class == type && readByte() == DataType.BOOLEAN) {
                field.set(obj, readBoolean());
            } else if (Object.class == type && readByte() == DataType.OBJECT) {
                field.set(obj, readObjectValue());
            } else if (String.class == type && readByte() == DataType.STRING) {
                field.set(obj, readString());
            } else if ((List.class == type || ArrayList.class == type) && readByte() == DataType.LIST) {
                ArrayListSerializer serializer = new ArrayListSerializer();
                field.set(obj, serializer.instance(this));
            } else if (Map.class == type && readByte() == DataType.MAP) {
                serializeContext.setSerializer(new MapSerializer());
                field.set(obj, serializeContext.instance(this));
            } else if (field.getType().isEnum() && readByte() == DataType.ENUM) {
                    field.set(obj, readEnum());
            } else if (field.getType().isArray() && readByte() == DataType.ARRAY) {
                Object value = readArray();
                field.set(obj, value);
            } else {
                field.set(obj, readObject());
            }
        }
    }

    public Object readArray() throws ClassNotFoundException {
        Object value = null;
        String tempClazz = (String) decoder.readClass();
        if (!tempClazz.equals("")) {
            Class componentType = Class.forName(tempClazz).getComponentType();
            if (!componentType.isArray()) {
                if (decoder.isPrimitiveArray(componentType)) {
                    value = decoder.readPrimitiveArray(componentType);
                } else {
                    int len = decoder.readInt();
                    Object[] array = (Object[]) Array.newInstance(componentType, len);
                    for (int i = 0; i < len; i++) {
                        array[i] = instanceTagData();
                    }
                    value = array;
                }
            } else {
                int len = decoder.readInt();
                Object[] array = (Object[]) Array.newInstance(componentType, len);
                for (int i = 0; i < len; i++) {
                    Object temp = readArray();
                    array[i] = temp;
                }
                value = array;
            }
        }
        return value;
    }

    public Object readEnum() throws ClassNotFoundException {
        Class clazz = Class.forName(readString());
        int ordinal = readInt();
        Object[] enumConstants = clazz.getEnumConstants();
        return enumConstants[ordinal];
    }

    public String readString(int size) {
        return decoder.readString(size);
    }

    public Object readField(Object obj) {

        return null;
    }

    public int readInt() {

        return decoder.readInt();
    }

    public long readLong() {
        return decoder.readLong();
    }

    public short readShort() {
        return decoder.readShort();
    }

    public byte readByte() {
        return decoder.readByte();
    }


    public char readChar() {
        return decoder.readChar();
    }

    public float readFloat() {
        return decoder.readFloat();
    }

    public double readDouble() {
        return decoder.readDouble();
    }

    public boolean readBoolean() {
        return decoder.readBoolean();
    }

    public String readString() {
        return decoder.readString();
    }

    public Object readObjectValue() {
        return decoder.readObjectValue();
    }
}
