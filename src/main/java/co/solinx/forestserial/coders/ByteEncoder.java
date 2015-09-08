package co.solinx.forestserial.coders;

import co.solinx.forestserial.common.ByteBufferTool;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.serializer.ClassInfo;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by linx on 2015/6/19.
 *
 */
public class ByteEncoder implements Encoder{

    ByteBuffer buffer=ByteBuffer.allocate(100);


    public static void main(String[] args) throws Exception {
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

    //todo  优化掉
    @Override
    public String toJsonString() {
        return null;
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
        buffer=ByteBufferTool.dilatation(buffer,1);
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


    @Override
    public void writeNull(){

    }

    public void writeList(List list){

    }

    /**
     * 基本类型数组
     * @param array
     * @param len
     */
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

    /**
     * 写入char数组
     * @param array
     * @param len
     */
    public void writeCharArray(char[] array,int len){
        increaseBuffer(2*len);
        for (int i = 0; i < len; i++) {
            buffer.putChar(array[i]);
        }
    }

    /**
     * 写入boolean数组
     * @param array
     * @param len
     */
    public void writeBooleanArray(boolean[] array,int len){
        increaseBuffer(1*len);
        for (int i = 0; i < len; i++) {
            buffer.put((byte) (array[i]?1:0));
        }
    }

    /**
     * 写入double数组
     * @param array
     * @param len
     */
    public void writeDoubleArray(double[] array,int len){
        increaseBuffer(len * 8);
        for (int i = 0; i < len; i++) {
            buffer.put(TypeToByteArray.doubleToByteArr(array[i]));
        }
    }

    /**
     * 写入float数组
     * @param array
     * @param len
     */
    public void writeFloatArray(float[] array,int len){
        increaseBuffer(len * 4);
        for (int i = 0; i < len; i++) {
            buffer.put(TypeToByteArray.floatToByteArr(array[i]));
        }
    }

    /**
     * 写入long数组
     * @param array
     * @param len
     */
    public void writeLongArray(long[] array,int len){
        increaseBuffer(len*8);
        for (int i = 0; i < len; i++) {
             buffer.put(TypeToByteArray.longToByteArr(array[i]));
        }
    }

    /**
     * 写入byte数组
     * @param array
     * @param len
     */
    public void writeByteArray(byte[] array,int len){
        increaseBuffer(len);
        buffer.put(array);
    }

    /**
     * 写入short数组
     * @param array
     * @param len
     */
    public void writeShortArray(short[] array,int len){
        increaseBuffer(len * 2);
        for (int i=0;i<len;i++){
            buffer.put(TypeToByteArray.shortToByteArr(array[i]));
        }
    }

    /**
     * 写入int数组
     * @param array
     * @param len
     */
    public void writeIntArray(int[] array,int len){
        int byteLen=len*4;
        increaseBuffer(byteLen);
        for (int i = 0; i < len; i++) {
            buffer.put(TypeToByteArray.intToByteArr(array[i]));
        }
    }

    /**
     * buffer扩容
     * @param len 扩容长度
     */
    public void increaseBuffer(int len){
        buffer=ByteBufferTool.dilatation(buffer,len);
    }

    /**
     * 是否为原生数组
     * @param componentType
     * @return
     */
    public boolean isPrimitiveArray(Class componentType){
        return componentType.isPrimitive();
    }



    public void writeString(String val){
        increaseBuffer(4 + val.getBytes().length);
        writeInt(val.getBytes().length);
        buffer.put(val.getBytes());
    }

}
