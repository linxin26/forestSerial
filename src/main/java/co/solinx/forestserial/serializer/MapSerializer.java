package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linx on 2015/7/31.
 */
public class MapSerializer {

    public void writeObject(ObjectOutput objectOutput, Field field, Object value, Encoder encoder){
        Map map= (Map) value;
        System.out.println("--------------------------- "+field.getType());
        System.out.println("--------------------------- "+field.getGenericType());

        for (Iterator iterator= map.entrySet().iterator();iterator.hasNext();){
            Map.Entry temp= (Map.Entry) iterator.next();
            System.out.println("+++++  "+temp.getKey().getClass());
        }
    }

}
