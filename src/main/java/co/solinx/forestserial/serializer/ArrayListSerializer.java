package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by linx on 2015/7/30.
 */
public class ArrayListSerializer implements Serializer{


    public void writeObject(Output objectOutput, Field field, Object value, Encoder encoder) throws Exception {
            ArrayList list = (ArrayList) value;
            encoder.writeInt(list.size());
            for (Object temp : list) {
                  objectOutput.writeObjectField(field,temp,temp.getClass());
            }
    }

    public Object instance(ObjectInput input){
        int size = input.readInt();
        ArrayList integerList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            integerList.add(input.instanceTagData());
        }
        return integerList;
    }

}
