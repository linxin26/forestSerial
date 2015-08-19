package co.solinx.forestserial.coders;

/**
 * Created by linx on 2015/6/29.
 */
public interface Decoder {

    Object decoder(byte[] byteData) throws Exception;

    Object readObject();

    Object readClass();

    String readString(int length);

    String readString();

    byte readByte();

    int readInt();

    long readLong();

    char readChar();

    short readShort();

    float readFloat();

    double readDouble();

    Object readObjectValue();

    boolean readBoolean();

    char[] readCharArray(int len);

    boolean[] readBooleanArray(int len);

    double[] readDoubleArray(int len);

    float[] readFloatArray(int len);

    long[] readLongArray(int len);

    short[] readShortArray(int len);

    byte[] readByteArray(int len);

    int[] readIntArray(int len);

}
