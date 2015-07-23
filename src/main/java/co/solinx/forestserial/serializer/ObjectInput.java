package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.util.FieldUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/7/23.
 */
public class ObjectInput {

    private ByteDecoder decoder;

    public ObjectInput(byte[] bytes){
        decoder=new ByteDecoder(bytes);
    }

    public Object readObject(){
        String className= (String) decoder.readObject();
        Object clazz=null;
        try {
               clazz= Class.forName(className).newInstance();
            readFields(clazz);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazz;
    }

    public Object readFields(Object obj) throws IllegalAccessException {

        Field[] fields = obj.getClass().getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField,obj);
        this.readObjectField(objectField,obj);
        return obj;
    }

    public void readPrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            if("int".equals(field.getType().getName()))
                field.set(obj, readInt());
        }
    }

    public void readObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field: fields){
            field.setAccessible(true);
            if("List".equals(field.getType().getSimpleName())|| "ArrayList".equals(field.getType().getSimpleName())){
                  byte flag= readByte();
                int type=readByte();
                System.out.println("type    " +String.valueOf(type&0xff  ));
                if(type==0x11) {
                    int size = readInt();
                    List<Integer> integerList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        integerList.add(readInt());
                    }
                    field.set(obj, integerList);
                }else if(type==0x12){
                    int size = readInt();
                    System.out.println(size);
                    List<String> integerList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        integerList.add(readString(readInt()));
                    }
                    field.set(obj, integerList);
                }
            }
        }
    }

    public String readString(int size){
        return decoder.readString(size);
    }
    public Object readField(Object obj){

        return null;
    }

    public int readInt(){

        return decoder.readInt();
    }

    public byte readByte(){
        return decoder.readByte();
    }


}
