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

    public ByteDecoder(){}
    public ByteDecoder(byte[] data){
        buffer=ByteBuffer.wrap(data,0,data.length);
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


    public Object readObject(){
        byte flag= readByte();
        int length=readInt();
        System.out.println("length ： " + length);
        String className= readString(length);

        System.out.println("length ： " + length + "  ClassName ：" + className);
        return className;
    }

    public String readString(int length){
        byte[] temp=new byte[length];
        buffer.get(temp);

        return new String(temp);
    }

    public String readString(){
        int length=readInt();
        String value= readString(length);
        return value;
    }

    public byte readByte(){

        return buffer.get();
    }

    public int readInt(){
        byte value=buffer.get();
        int result;
        if(value>-127 && value<=127){
            result = value;
        } else if (value == -128) {
            result= buffer.getShort();
        }else{
            result= buffer.getInt();
        }
        return result;
    }

    public long readLong(){
        byte value=buffer.get();
        long result;
        if(value>-126&& value<=127){
            result= value;
        }else if(value==-128){
            result= buffer.getShort();
        }else if(value==-127){
            result= buffer.getInt();
        }else{
            result=  buffer.getLong();
        }
        return result;
    }


    public char readChar(){
        byte value=buffer.get();
        char result;
        if(value<255&&value>=0){
            result= (char) value;
        }else{
            result=buffer.getChar();
        }
        return result;
    }

    public short readShort(){
        byte value=buffer.get();
        short result;
        if(value>-127&& value<=127){
            result= value;
        }else{
            result= buffer.getShort();
        }
        return result;
    }

    public float readFloat(){
        int value=buffer.getInt();
        return Float.intBitsToFloat(value);
    }

    public double readDouble(){
        long value=buffer.getLong();
        return Double.longBitsToDouble(value);
    }

    public Object readObjectValue(){
        int length=buffer.getInt();
        return new String(readString(length));
    }

    public boolean readBoolean(){
        byte value=buffer.get();
        return value==1?true:false;
    }


    public boolean isPrimitiveArray(Class componentType){
        return componentType.isPrimitive();
    }

    public Object readPrimitiveArray(Class componentType) {
        int len= readInt();
        Object array = null;
        if(componentType==int.class){
            array=readIntArray(len);
        }
        if(componentType==byte.class){
            array=readByteArray(len);
        }
        return array;
    }

    public byte[] readByteArray(int len){
        byte[] byteArray=new byte[len];
        buffer.get(byteArray);
        return byteArray;
    }

    public int[] readIntArray(int len){
        int[] intArray=new int[len];
        for (int i = 0; i < len; i++) {
            byte[] temp=new byte[4];
            buffer.get(temp);
            intArray[i]= TypeToByteArray.hBytesToInt(temp);
        }
        System.out.println(len+"====================== "+Arrays.toString(intArray));
        return intArray;
    }
}
