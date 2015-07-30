package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by linx on 2015/7/30.
 */
public class ArrayListSerializer {


    public void writeObject(ObjectOutput objectOutput, Field field, Object value, Encoder encoder) throws IllegalAccessException {
            ArrayList list = (ArrayList) value;
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
                String typeName=clazz.getTypeName();
                if ("java.lang.Integer".equals(typeName)) {
                    encoder.writeByte((byte) 0x11);
                } else if ("java.lang.String".equals(typeName)) {
                    encoder.writeByte((byte) 0x12);
                } else if ("java.lang.Long".equals(typeName)) {
                    encoder.writeByte((byte) 0x13);
                }else if("java.lang.Character".equals(typeName)){
                    encoder.writeByte((byte) 0x14);
                }
            }
            encoder.writeInt(list.size());

            for (Object temp : list) {
                  objectOutput.writeObjectField(field,temp,temp.getClass().getSimpleName());
            }
    }

}
