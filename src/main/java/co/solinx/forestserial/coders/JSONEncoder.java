package co.solinx.forestserial.coders;

import java.util.List;

/**
 * Created by lin8x_000 on 2015-08-17.
 */
public class JSONEncoder implements Encoder {

    @Override
    public byte[] encoder(Object obj) {
        return new byte[0];
    }

    @Override
    public void writeClass(Class clazz) {

    }

    @Override
    public void writeTag(byte tag) {

    }

    @Override
    public void writeInt(int val) {

    }

    @Override
    public void writeLong(long val) {

    }

    @Override
    public void writeString(String val) {

    }

    @Override
    public void writeByte(byte val) {

    }

    @Override
    public void writeShort(short val) {

    }

    @Override
    public void writeChar(char val) {

    }

    @Override
    public void writeFloat(float val) {

    }

    @Override
    public void writeDouble(double val) {

    }

    @Override
    public void writeBoolean(boolean val) {

    }

    @Override
    public void writeObject(Object obj) {

    }

    @Override
    public void writeList(List list) {

    }

    @Override
    public void writePrimitiveArray(Object array, int len) {

    }

    @Override
    public void writeByteArray(byte[] array, int len) {

    }

    @Override
    public void writeIntArray(int[] array, int len) {

    }

    @Override
    public boolean isPrimitiveArray(Class componentType) {
        return false;
    }

    @Override
    public byte[] toByte() {
        return new byte[0];
    }
}
