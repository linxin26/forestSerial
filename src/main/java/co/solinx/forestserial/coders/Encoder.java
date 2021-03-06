package co.solinx.forestserial.coders;

import java.util.List;

/**
 * Created by linx on 2015/7/1.
 */
public interface Encoder {

    byte[] encoder(Object obj);

    void writeClass(Class clazz);

    void writeTag(byte tag);

    void writeInt(int val);

    void writeLong(long val);

    void writeString(String val);

    void writeSymbol(String val);

    void writeFieldName(String val);

    void writeByte(byte val);

    void writeNull();

    void writeShort(short val);

    void writeChar(char val);

    void writeFloat(float val);

    void writeDouble(double val);

     void writeBoolean(boolean val);

     void writeObject(Object obj);

    void writeList(List list);

    void writePrimitiveArray(Object array, int len);

    void writeByteArray(byte[] array,int len);

    void replaceLastSymbol(String old,char news);

    void writeIntArray(int[] array,int len);

    void writeShortArray(short[] array,int len);

    void  writeLongArray(long[] array,int len);

    void   writeFloatArray(float[] array,int len);

    void writeDoubleArray(double[] array,int len);

    void writeBooleanArray(boolean[] array,int len);

    void writeCharArray(char[] array,int  len);

    void writeStringArray(String[] val);

    boolean isPrimitiveArray(Class componentType);

     byte[] toByte();

    String toJsonString();

}
