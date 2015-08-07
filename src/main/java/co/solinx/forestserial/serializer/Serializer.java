package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;

/**
 * Created by linx on 2015/8/6.
 */
public interface Serializer {

    void writeObject(ObjectOutput output, Field field, Object value, Encoder encoder) throws Exception ;

    Object instance(ObjectInput input);

}
