package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linx on 2015/7/31.
 */
public class MapSerializer {

    public void writeObject(ObjectOutput objectOutput, Field field, Object value, Encoder encoder) throws IllegalAccessException {
        Map map= (Map) value;
        encoder.writeInt(map.size());
        for (Iterator iterator= map.entrySet().iterator();iterator.hasNext();){
            Map.Entry temp= (Map.Entry) iterator.next();
            objectOutput.writeObjectField(field,temp.getKey(),temp.getKey().getClass());
            objectOutput.writeObjectField(field,temp.getValue(),temp.getValue().getClass());
        }
    }

}
