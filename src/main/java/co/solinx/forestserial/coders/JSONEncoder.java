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
//        jsonString+=clazz.getSimpleName()+"{";
        jsonString+="{";
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
        jsonString+="\""+val+"\""+",";
    }

    public void writeFieldName(String val){
        jsonString+="\""+val+"\""+":";
    }

    @Override
    public void writeSymbol(String val) {
        jsonString+=val;
    }

    public void writeStringArray(String val){

    }

    @Override
    public void writeByte(byte val) {
        jsonString+=val+",";
    }


    @Override
    public void writeNull(){
        jsonString+="null,";
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
        Class type=array.getClass().getComponentType();
        if(type==byte.class){
            writeByteArray((byte[]) array,len);
        }
        if(type==int.class){
            writeIntArray((int[]) array,len);
        }
        if(type==short.class){
            writeShortArray((short[]) array, len);
        }
        if(type==long.class){
            writeLongArray((long[]) array, len);
        }
        if(type==float.class){
            writeFloatArray((float[]) array,len);
        }
        if(type==double.class){
            writeDoubleArray((double[]) array,len);
        }
        if(type==boolean.class){
            writeBooleanArray((boolean[]) array,len);
        }
        if(type==char.class){
            writeCharArray((char[]) array, len);
        }
    }

    @Override
    public void writeByteArray(byte[] array, int len) {

    }

    public void replaceLastSymbol(String old,char news){
        int oldIndex=jsonString.lastIndexOf(old);
        int lastIndex=jsonString.length()-1;
        if(oldIndex==lastIndex){
            char[] temp = jsonString.toCharArray();
            temp[oldIndex] =news;
            jsonString = new String(temp);
        }
    }

    @Override
    public void writeIntArray(int[] array, int len) {
        jsonString+="[";
            for (int i = 0; i < array.length; i++) {
               jsonString+=array[i];
                if(i!=array.length-1){
                    jsonString+=",";
                }
            }
        jsonString+="],";
    }

    @Override
    public void writeShortArray(short[] array, int len) {

    }

    @Override
    public void writeLongArray(long[] array, int len) {

    }

    @Override
    public void writeFloatArray(float[] array, int len) {

    }

    @Override
    public void writeDoubleArray(double[] array, int len) {

    }

    @Override
    public void writeBooleanArray(boolean[] array, int len) {

    }

    @Override
    public void writeCharArray(char[] array, int len) {

    }

    @Override
    public void writeStringArray(String[] array) {
        jsonString+="[";
        for (int i = 0; i < array.length; i++) {
            jsonString+="\""+array[i]+"\"";
            if(i!=array.length-1){
                jsonString+=",";
            }
        }
        jsonString+="],";
    }

    @Override
    public boolean isPrimitiveArray(Class componentType) {
        return componentType.isPrimitive();
    }

    @Override
    public byte[] toByte() {
        return new byte[0];
    }



    @Override
    public String toJsonString() {

        this.replaceLastSymbol(",",'}');

        return jsonString;
    }
}
