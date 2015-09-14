package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;

/**
 * Created by linx on 2015/8/6.
 */
public class SerializeContext {

    private Serializer serializer;

    public void setSerializer(Serializer seriali) {
        serializer = seriali;
    }

    public void writeObject(Output output, Field field, Object value, Encoder encoder) throws Exception {
        serializer.writeObject(output, field, value, encoder);
    }

    public Object instance(ObjectInput input) {
        return serializer.instance(input);
    }

}
