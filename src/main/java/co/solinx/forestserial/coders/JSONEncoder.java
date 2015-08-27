package co.solinx.forestserial.coders;

import co.solinx.forestserial.serializer.ClassInfo;

import java.util.List;

/**
 * Created by lin8x_000 on 2015-08-17.
 * json编码
 */
public class JSONEncoder implements Encoder {

    String jsonString="";
    @Override
    public byte[] encoder(Object obj) {
        return new byte[0];
    }

    public String encoderObject(Object obj){
        ClassInfo classInfo=new ClassInfo(obj);
         jsonString=classInfo.getClassName()+"{";
        classInfo.getDeclaredFields();
        return jsonString;
    }

    @Override
    public void writeClass(Class clazz) {
        jsonString+=clazz.getSimpleName()+"{";
    }

    @Override
    public void writeTag(byte tag) {
        System.out.println("tag ");
    }

    @Override
    public void writeInt(int val) {
//        System.out.println(jsonString+" int  "+val);
        jsonString+=val+",";
    }

    @Override
    public void writeLong(long val) {
        jsonString+=val+",";
    }

    @Override
    public void writeString(String val) {
        System.out.println(" String  "+val);
        jsonString+=val+":";
    }

    @Override
    public void writeByte(byte val) {
        jsonString+=val+",";
    }

    @Override
    public void writeShort(short val) {
        jsonString+=val+",";
    }

    @Override
    public void writeChar(char val) {
        jsonString+=val+",";
    }

    @Override
    public void writeFloat(float val) {
        jsonString+=val+",";
    }

    @Override
    public void writeDouble(double val) {
        jsonString+=val+",";
    }

    @Override
    public void writeBoolean(boolean val) {
        jsonString+=val+",";
    }

    @Override
    public void writeObject(Object obj) {
//        jsonString+=obj+",";
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

    @Override
    public String toJsonString() {
        return jsonString;
    }
}
