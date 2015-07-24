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

    public void readObject(Object obj,Class cla) throws IllegalAccessException {
        String className= (String) decoder.readObject();
            readFields(obj,cla);

    }

    public Object readFields(Object obj) throws IllegalAccessException {

        Field[] fields = obj.getClass().getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField, obj);
        this.readObjectField(objectField, obj);

       readSuperClass(obj);

        return obj;
    }

    public Object readFields(Object obj,Class clazz) throws IllegalAccessException {

        Field[] fields = clazz.getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField, obj);
        this.readObjectField(objectField, obj);


        return obj;
    }

    public void readSuperClass(Object obj) throws IllegalAccessException {
        System.out.println(obj.getClass().getSuperclass().getName());

        readObject(obj, obj.getClass().getSuperclass());
//        readFields(obj,obj.getClass().getSuperclass());
    }


    public void readPrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            if("int".equals(field.getType().getName())) {
                field.set(obj, readInt());
            }else if("long".equals(field.getType().getName())){
                field.set(obj,readLong());
            }else if("short".equals(field.getType().getName())){
                field.set(obj,readShort());
            }else if("byte".equals(field.getType().getName())){
                field.set(obj,readByte());
            }
        }
    }

    public void readObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field: fields){
            field.setAccessible(true);
            String typeName=field.getType().getSimpleName();
            if("Integer".equals(typeName)){
                field.set(obj,readInt());
            }else if("Short".equals(typeName)){
                field.set(obj,readShort());
            }else if("Long".equals(typeName)){
                Long value=readLong();

                field.set(obj,value);
            }else if("Byte".equals(typeName)){
                field.set(obj,readByte());
            }else if("Character".equals(typeName)){

            }else if("Float".equals(typeName)){

            }else if("Double".equals(typeName)){

            }else if("Boolean".equals(typeName)){

            }else if("Object".equals(typeName)){

            }else if("String".equals(typeName)){

            }else if("List".equals(typeName)|| "ArrayList".equals(typeName)){
                  byte flag= readByte();
                int type=readByte();
                if(type==0x11) {
                    int size = readInt();
                    List<Integer> integerList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        integerList.add(readInt());
                    }
                    field.set(obj, integerList);
                }else if(type==0x12){
                    int size = readInt();
                    List<String> integerList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        integerList.add(readString(readInt()));
                    }
                    field.set(obj, integerList);
                }
            }else{
                field.set(obj, readObject());
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

    public long readLong(){
        return decoder.readLong();
    }

    public short readShort(){
        return decoder.readShort();
    }

    public byte readByte(){
        return decoder.readByte();
    }


}
