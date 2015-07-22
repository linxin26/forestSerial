package co.solinx.forestserial.coders;

/**
 * Created by linx on 2015/7/1.
 */
public interface Encoder {

    byte[] encoder(Object obj);

    void writeClass(Class clazz);

    void writeTag(byte tag);

     byte[] toByte();

}
