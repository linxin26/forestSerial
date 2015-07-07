package co.solinx.forestserial.Buffer;

import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/7/2.
 */
public class BufferStream {

    ByteBuffer byteBuffer;

    public BufferStream(byte[] dataByte) {
        byteBuffer = ByteBuffer.wrap(dataByte);
    }


    public int getPosition(){
        return byteBuffer.position();
    }

    /**
     * getString
     *
     * @param length 长度
     * @return 字符串
     */
    public String getString(int length) {
        byte[] str = new byte[length];
        byteBuffer.get(str);
        return new String(str);
    }

    public boolean hasRemaining(){
        return byteBuffer.hasRemaining();
    }

    public byte getByte() {
        return byteBuffer.get();
    }

    public void getByte(byte[] bytes){
        byteBuffer.get(bytes);
    }

    public int getInt() {
        return byteBuffer.getInt();
    }

    public short getShort() {
        return byteBuffer.getShort();
    }

    public long getLong() {
        return byteBuffer.getLong();
    }

    public char getChar() {
        return byteBuffer.getChar();
    }

    public float getFloat() {
        return byteBuffer.getFloat();
    }

    public double getDouble() {
        return byteBuffer.getDouble();
    }


}
