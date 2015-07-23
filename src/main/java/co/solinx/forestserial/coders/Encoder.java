package co.solinx.forestserial.coders;

import java.util.List;

/**
 * Created by linx on 2015/7/1.
 */
public interface Encoder {

    byte[] encoder(Object obj);

    void writeClass(Class clazz);

    void writeTag(byte tag);

    void writeInt(int val);

    void writeString(String val);

    void writeByte(byte val);

    void writeList(List list);

     byte[] toByte();

}
