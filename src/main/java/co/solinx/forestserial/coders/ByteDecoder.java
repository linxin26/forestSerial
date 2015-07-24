package co.solinx.forestserial.coders;

import co.solinx.forestserial.serializer.ClassInfo;

import java.nio.ByteBuffer;

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
        System.out.println("length ： " + length );
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

        return null;
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

        return buffer.getFloat();
    }
}
