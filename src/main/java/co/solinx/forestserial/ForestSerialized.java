package co.solinx.forestserial;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.coders.ByteEncoder;

/**
 * Created by linx on 2015/7/20.
 */
public class ForestSerialized {

    ByteEncoder encoder = new ByteEncoder();
    ByteDecoder decoder = new ByteDecoder();

    public byte[] serialize(Object obj) {

        return encoder.encoder(obj);
    }


    public Object deSerialize(byte[] data) throws Exception {

        return decoder.decoder(data);
    }


}