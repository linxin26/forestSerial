package co.solinx.forestserial.coders;

import co.solinx.forestserial.common.ByteBufferTool;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.data.Response;
import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.serializer.ClassInfo;
import co.solinx.forestserial.util.StringUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder implements Encoder{

    ByteBuffer buffer=ByteBuffer.allocate(100);


    public static void main(String[] args) throws Exception {
        Test request = new Test();
        request.setA(10);
        request.setB(5);
        request.setCc(254);
        request.setBl(false);
        request.setBt((byte) 12);
        request.setCr('A');
        request.setSt((short) 27);
        request.setFl(12.3f);
        request.setDl(13.2);
        request.setBa("Ba");
        request.setBb("Bb");
        request.setInte(132);
        request.setLongNum(345l);
        request.setCharNum('B');
        request.setBooleanNum(true);
        request.setFloatNum(19.5f);
        request.setDoubleNum(20.1d);
        request.setShortNum((short) 21);
        request.setByteNum((byte) 29);
        request.setStringString("Stringing");
        request.setZzzz(9999999l);
        request.setObj(12);

        Response response = new Response();
        response.setResult("类字段");
        response.setSn(998);
        response.setBaseID(188);

        request.setResponse(response);
        request.setBaseClassID(190000);
        request.setResult("result");
        request.setSuperSn(18880);
        request.setBaseID(199);

        ByteEncoder encoder = new ByteEncoder();
        ByteDecoder decoder = new ByteDecoder();
        byte[] dataByte = encoder.encoder(request);
        Test temp = (Test) decoder.decoder(dataByte);

        System.out.println(temp.toString());
    }



    /**
     * 对象转码为Byte[]
     *
     * @param obj 对象
     * @return 返回编码后的byte数组
     */
    public byte[] encoder(Object obj) {
        /**
         * Byte[]=标志+类名长度+类名+原始类型+引用类型+父类
         * 每个引用类型
         */


        ClassInfo classInfo=new ClassInfo(obj);

        byte[] classNameByte = classInfo.getClassNameByte();

        //父类
        Class superClass = obj.getClass().getSuperclass();
        //父类
        byte[] superByte = classInfo.superClassToByte(obj, superClass);

        byte[] fieldByte = classInfo.fieldBytes( );


        System.out.println("类名：" + StringUtil.bytesToString(classNameByte));

        ByteBuffer fieldBuf = ByteBuffer.allocate(1+fieldByte.length + superByte.length + classNameByte.length);
        byte[] flag=new byte[1];
        flag[0]= DataType.CLASS_NAME;
        fieldBuf.put(flag);
        fieldBuf.put(classNameByte);
        fieldBuf.put(fieldByte);
        fieldBuf.put(superByte);
        System.out.println("合并后：" + StringUtil.bytesToString(fieldBuf.array()));
        return fieldBuf.array();
    }

    public void writeClass(Class clazz){
          byte[] names= clazz.getName().getBytes();
         writeInt(names.length);
        buffer= ByteBufferTool.put(buffer,names);
    }



    public void writeTag(byte tag){
        buffer=ByteBufferTool.dilatation(buffer,1);
        buffer.put(tag);
    }

    public void writePrimitiveField(Field fields,Object obj){

    }

    public byte[] toByte(){
        byte[] bytes=new byte[buffer.capacity() - buffer.remaining()];
      System.arraycopy(buffer.array(), 0, bytes, 0, bytes.length);
        return bytes;
    }

    public void writeInt(int val) {

        buffer=ByteBufferTool.dilatation(buffer,5);
        // -128 = short byte, -127 == 4 byte
        if (val > -127 && val <= 127) {
            buffer.put((byte) val);
        }else if(val>=Short.MIN_VALUE && val<=Short.MAX_VALUE){
            buffer.put((byte) -128);
            buffer.putShort((short) val);
        } else {
            buffer.put((byte) -127);
            buffer.putInt(val);
        }
    }

    public void writeShort(short val){
        buffer=ByteBufferTool.dilatation(buffer,3);
        if(val>-127&& val<=127){
            buffer.put((byte) val);
        }else{
            buffer.put((byte) -128);
            buffer.putShort(val);
        }
    }

    public void writeFloat(float val){
        buffer=ByteBufferTool.dilatation(buffer,4);
        buffer.putInt(Float.floatToIntBits(val));
    }

    public void writeDouble(double val){
        buffer=ByteBufferTool.dilatation(buffer,8);
        buffer.putLong(Double.doubleToLongBits(val));
    }

    public void writeChar(char val){
        buffer=ByteBufferTool.dilatation(buffer,2);
        if(val<255&val>=0){
            buffer.put((byte) val);
        }else{
            buffer.put((byte) 255);
            buffer.putChar(val);
        }
    }

    public void writeBoolean(boolean val){
        buffer.put((byte) (val?1:0));
    }

    public void writeObject(Object obj){
        System.out.println("_____________________"+obj.toString().getBytes().length);
        System.out.println(obj);
        buffer=ByteBufferTool.dilatation(buffer,obj.toString().getBytes().length+4);
        buffer.putInt(obj.toString().getBytes().length);
        buffer.put(obj.toString().getBytes());
    }

    public void writeLong(long val){
        buffer=ByteBufferTool.dilatation(buffer,9);
         if(val>-127 && val<=127){
             buffer=ByteBufferTool.put(buffer,new byte[]{(byte) val});
         }else if(val>=Short.MIN_VALUE&& val<=Short.MAX_VALUE){
             buffer.put((byte) -128);
             buffer.putShort((short) val);
         }else if(val>=Integer.MIN_VALUE&& val<=Integer.MAX_VALUE){
            buffer.put((byte) -127);
             buffer.putInt((int) val);
         }else{
             buffer.put((byte) -126);
             buffer.putLong(val);
         }
    }

    public void writeByte(byte val){
        buffer=ByteBufferTool.dilatation(buffer,1);
        buffer.put(val);
    }




    public void writeList(List list){

    }

    public void writeString(String val){
        buffer=ByteBufferTool.dilatation(buffer, 4 + val.getBytes().length);
        writeInt(val.getBytes().length);
        buffer.put(val.getBytes());
    }

}
