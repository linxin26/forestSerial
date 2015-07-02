package co.solinx.forestserial.Buffer;

import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/7/2.
 */
public class BufferStream {

    ByteBuffer byteBuffer;
    public BufferStream(byte[] dataByte){
        byteBuffer=ByteBuffer.wrap(dataByte);
    }


}
