package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linx on 2015/7/31.
 */
public class MapSerializer implements Serializer{

    public void writeObject(ObjectOutput objectOutput, Field field, Object value, Encoder encoder) throws Exception {
        Map map= (Map) value;
        encoder.writeInt(map.size());
        for (Iterator iterator= map.entrySet().iterator();iterator.hasNext();){
            Map.Entry temp= (Map.Entry) iterator.next();
            objectOutput.writeObjectField(field,temp.getKey(),temp.getKey().getClass());
            objectOutput.writeObjectField(field,temp.getValue(),temp.getValue().getClass());
        }
    }

    public Object instance(ObjectInput input){
        int size=input.readInt();
        Map map=new HashMap<>(size);
        for (int i=0;i<size;i++){
            Object key,value;
            key=input.instanceTagData();
            value=input.instanceTagData();
            map.put(key,value);
        }

        return map;
    }

}
