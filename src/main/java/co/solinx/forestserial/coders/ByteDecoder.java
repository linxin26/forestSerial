package co.solinx.forestserial.coders;

import co.solinx.forestserial.serializer.ClassInfo;
import co.solinx.forestserial.util.TypeToByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by linx on 2015-06-22.
 */
public class ByteDecoder implements Decoder {

    ByteBuffer buffer;

    public ByteDecoder() {
    }

    public ByteDecoder(byte[] data) {
        buffer = ByteBuffer.wrap(data, 0, data.length);
    }


    public Object decoder(byte[] byteData) throws Exception {


        System.out.println("-------------------------------decoder-------------------------");

        ClassInfo classInfo = new ClassInfo(byteData);
        Object clazz = classInfo.getInstanceObject();
        //解码字段
        classInfo.deField();

        //解码父类
        classInfo.superClassDeCode(clazz, clazz.getClass().getSuperclass());

        return clazz;
    }


    public Object readObject() {
        byte flag = readByte();
        int length = readInt();
        String className = readString(length);
//        System.out.println("length ： " + length + "  ClassName ：" + className);
        return className;
    }

    public Object readClass() {
        int length = readInt();
        String className = readString(length);
        return className;
    }

    public String readString(int length) {
        byte[] temp = new byte[length];
        buffer.get(temp);

        return new String(temp);
    }

    public String readString() {
        int length = readInt();
        String value = readString(length);
        return value;
    }

    public byte readByte() {

        return buffer.get();
    }

    public int readInt() {
        byte value = buffer.get();
        int result;
        if (value > -127 && value <= 127) {
            result = value;
        } else if (value == -128) {
            result = buffer.getShort();
        } else {
            result = buffer.getInt();
        }
        return result;
    }

    public long readLong() {
        byte value = buffer.get();
        long result;
        if (value > -126 && value <= 127) {
            result = value;
        } else if (value == -128) {
            result = buffer.getShort();
        } else if (value == -127) {
            result = buffer.getInt();
        } else {
            result = buffer.getLong();
        }
        return result;
    }


    public char readChar() {
        byte value = buffer.get();
        char result;
        if (value < 255 && value >= 0) {
            result = (char) value;
        } else {
            result = buffer.getChar();
        }
        return result;
    }

    public short readShort() {
        byte value = buffer.get();
        short result;
        if (value > -127 && value <= 127) {
            result = value;
        } else {
            result = buffer.getShort();
        }
        return result;
    }

    public float readFloat() {
        int value = buffer.getInt();
        return Float.intBitsToFloat(value);
    }

    public double readDouble() {
        long value = buffer.getLong();
        return Double.longBitsToDouble(value);
    }

    public Object readObjectValue() {
        int length = buffer.getInt();
        return new String(readString(length));
    }

    public boolean readBoolean() {
        byte value = buffer.get();
        return value == 1 ? true : false;
    }


    public boolean isPrimitiveArray(Class componentType) {
        return componentType.isPrimitive();
    }

    /**
     * 读取基本类型数组
     *
     * @param type
     * @return
     */
    public Object readPrimitiveArray(Class type) {
        int len = readInt();
        Object array = null;
        if (type == int.class) {
            array = readIntArray(len);
        }
        if (type == byte.class) {
            array = readByteArray(len);
        }
        if (type == short.class) {
            array = readShortArray(len);
        }
        if (type == long.class) {
            array = readLongArray(len);
        }
        if (type == float.class) {
            array = readFloatArray(len);
        }
        if (type == double.class) {
            array = readDoubleArray(len);
        }
        if (type == boolean.class) {
            array = readBooleanArray(len);
        }
        if (type == char.class) {
            array = readCharArray(len);
        }
        return array;
    }

    /**
     * 读取添加char数组
     *
     * @param len
     * @return
     */
    public char[] readCharArray(int len) {
        char[] charArray = new char[len];
        for (int i = 0; i < len; i++) {
            charArray[i] = buffer.getChar();
        }
        return charArray;
    }

    /**
     * 读取boolean数组
     *
     * @param len
     * @return
     */
    public boolean[] readBooleanArray(int len) {
        boolean[] booleanArray = new boolean[len];
        for (int i = 0; i < len; i++) {
            byte temp = buffer.get();
            if (temp == 1) {
                booleanArray[i] = true;
            } else {
                booleanArray[i] = false;
            }
        }
        return booleanArray;
    }

    /**
     * 读取double数组
     *
     * @param len
     * @return
     */
    public double[] readDoubleArray(int len) {
        double[] doubleArray = new double[len];
        for (int i = 0; i < len; i++) {
            byte[] temp = new byte[8];
            buffer.get(temp);
            doubleArray[i] = TypeToByteArray.getDouble(temp);
        }
        return doubleArray;
    }

    /**
     * 读取float数组
     *
     * @param len
     * @return
     */
    public float[] readFloatArray(int len) {
        float[] floatArray = new float[len];
        for (int i = 0; i < len; i++) {
            byte[] temp = new byte[4];
            buffer.get(temp);
            floatArray[i] = TypeToByteArray.getFloat(temp);
        }
        return floatArray;
    }

    /**
     * 读取long数组
     *
     * @param len
     * @return
     */
    public long[] readLongArray(int len) {
        long[] longArray = new long[len];
        for (int i = 0; i < len; i++) {
            byte[] temp = new byte[8];
            buffer.get(temp);
            longArray[i] = TypeToByteArray.getLong(temp);
        }
        return longArray;
    }

    /**
     * 读取short数组
     *
     * @param len
     * @return
     */
    public short[] readShortArray(int len) {
        short[] shortArray = new short[len];
        for (int i = 0; i < len; i++) {
            byte[] temp = new byte[2];
            buffer.get(temp);
            shortArray[i] = TypeToByteArray.hBytesToShort(temp);
        }
        return shortArray;
    }

    /**
     * 读取byte数组
     *
     * @param len
     * @return
     */
    public byte[] readByteArray(int len) {
        byte[] byteArray = new byte[len];
        buffer.get(byteArray);
        return byteArray;
    }

    /**
     * 读取int数组
     *
     * @param len
     * @return
     */
    public int[] readIntArray(int len) {
        int[] intArray = new int[len];
        for (int i = 0; i < len; i++) {
            byte[] temp = new byte[4];
            buffer.get(temp);
            intArray[i] = TypeToByteArray.hBytesToInt(temp);
        }
        return intArray;
    }
}
