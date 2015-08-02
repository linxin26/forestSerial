package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.Encoder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/7/30.
 */
public class ArrayListSerializer {


    public void writeObject(ObjectOutput objectOutput, Field field, Object value, Encoder encoder) throws IllegalAccessException {
            ArrayList list = (ArrayList) value;
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (Integer.class==clazz) {
                    encoder.writeByte((byte) 0x11);
                } else if (String.class==clazz) {
                    encoder.writeByte((byte) 0x12);
                } else if (Long.class==clazz) {
                    encoder.writeByte((byte) 0x13);
                }else if(Character.class==clazz){
                    encoder.writeByte((byte) 0x14);
                }else if(Short.class==clazz){
                    encoder.writeByte((byte) 0x15);
                }else if(Byte.class==clazz){
                    encoder.writeByte((byte) 0x16);
                }else if(Float.class==clazz){
                    encoder.writeByte((byte) 0x17);
                }else if(Double.class==clazz){
                    encoder.writeByte((byte) 0x18);
                }else if(Boolean.class==clazz){
                    encoder.writeByte((byte) 0x19);
                }
            }
            encoder.writeInt(list.size());
            for (Object temp : list) {
                  objectOutput.writeObjectField(field,temp,temp.getClass());
            }
    }

    public Object instance(ObjectInput input){
        byte flag = input.readByte();
        int typeItem = input.readByte();
        int size = input.readInt();
        ArrayList integerList = new ArrayList<>(size);
        if (typeItem == 0x11) {
            for (int i = 0; i < size; i++) {
                input.readByte();
                integerList.add(input.readInt());
            }
        } else if (typeItem == 0x12) {
            for (int i = 0; i < size; i++) {
                input.readByte();
                integerList.add(input.readString(input.readInt()));
            }
        }else if (typeItem == 0x13) {
            for (int i = 0; i < size; i++) {
                input.readByte();
                integerList.add(input.readLong());
            }
        }else if(typeItem==0x14){
            for (int i = 0; i < size; i++) {
                input.readByte();
                integerList.add(input.readChar());
            }
        }
        return integerList;
    }

}
