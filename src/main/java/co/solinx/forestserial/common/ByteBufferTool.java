package co.solinx.forestserial.common;

import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/7/2.
 */
public class ByteBufferTool {

    /**
     * byteBuffer自定义Put方法，支持Buffer的自动扩容
     *
     * @param byteBuf 传入Buffer
     * @param bytes   返回Buffer
     * @return 返回新的ByteBuffer
     */
    public static java.nio.ByteBuffer put(java.nio.ByteBuffer byteBuf, byte[] bytes) {
        java.nio.ByteBuffer tempBuf;
        //检查buffer剩余容量
        if (byteBuf.remaining() < bytes.length) {
            //剩余容量不够，扩大bytes.length个长度
            tempBuf = java.nio.ByteBuffer.allocate(byteBuf.array().length + bytes.length);
            byte[] curByte = new byte[byteBuf.position()];
            //把原本的数据拷贝到curByte中
            System.arraycopy(byteBuf.array(), 0, curByte, 0, byteBuf.position());

            tempBuf.put(curByte);
            tempBuf.put(bytes);
        } else {
            tempBuf = java.nio.ByteBuffer.allocate(byteBuf.array().length);
            byte[] temp = new byte[byteBuf.position()];
            System.arraycopy(byteBuf.array(), 0, temp, 0, byteBuf.position());
            tempBuf.put(temp);
            tempBuf.put(bytes);
        }
        return tempBuf;
    }

    public static ByteBuffer dilatation(ByteBuffer byteBuf,int size){
        ByteBuffer tempBuf=byteBuf;
        if(byteBuf.remaining()<size){
            tempBuf=ByteBuffer.allocate(byteBuf.array().length+size);
            byte[] curByte=new byte[byteBuf.position()];
            System.arraycopy(byteBuf.array(),0,curByte,0,byteBuf.position());
            tempBuf.put(curByte);
        }
        return tempBuf;
    }

}
