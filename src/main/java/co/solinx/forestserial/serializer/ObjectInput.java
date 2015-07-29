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
            readFields(obj, cla);

    }

    public Object readFields(Object obj) throws IllegalAccessException {

        Field[] fields = obj.getClass().getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);


        this.readPrimitiveField(primitiveField, obj);
        this.readObjectField(objectField, obj);

       readSuperClass(obj, obj.getClass().getSuperclass());

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

    public void readSuperClass(Object obj,Class superClass) throws IllegalAccessException {
//        System.out.println("read  " + superClass.getName());

       if(!"Object".equals(superClass.getSimpleName())) {
           readSuperClass(obj,superClass.getSuperclass());
           readObject(obj, superClass);

       }

//        readFields(obj,obj.getClass().getSuperclass());
    }


    public void readPrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            String type=field.getType().getName();
            if("int".equals(type)) {
                field.set(obj, readInt());
            }else if("long".equals(type)){
                field.set(obj,readLong());
            }else if("short".equals(type)){
                field.set(obj,readShort());
            }else if("byte".equals(type)){
                field.set(obj,readByte());
            }else if("char".equals(type)){
                field.set(obj,readChar());
            }else if("float".equals(type)){
                field.set(obj,readFloat());
            }else if("double".equals(type)){
                field.set(obj,readDouble());
            }else if("boolean".equals(type)){
                field.set(obj,readBoolean());
            }
        }
    }

    public void readObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field: fields){
            field.setAccessible(true);
            String typeName=field.getType().getSimpleName();
            if("Integer".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readInt());
                }
            }else if("Short".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readShort());
                }
            }else if("Long".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readLong());
                }
            }else if("Byte".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readByte());
                }
            }else if("Character".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readChar());
                }
            }else if("Float".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readFloat());
                }
            }else if("Double".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readDouble());
                }
            }else if("Boolean".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readBoolean());
                }
            }else if("Object".equals(typeName)){
                System.out.println("--------------------------------------------");
                System.out.println(field);
                if(readByte()==1) {
                    field.set(obj, readObjectValue());
                }
            }else if("String".equals(typeName)){
                if(readByte()==1) {
                    field.set(obj, readString());
                }
            }else if("List".equals(typeName)|| "ArrayList".equals(typeName)){
                if(readByte()==1) {

                    byte flag = readByte();
                    int type = readByte();
                    int size = readInt();
                    if (type == 0x11) {

                        List<Integer> integerList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            integerList.add(readInt());
                        }
                        field.set(obj, integerList);
                    } else if (type == 0x12) {
                        List<String> integerList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            integerList.add(readString(readInt()));
                        }
                        field.set(obj, integerList);
                    }else if (type == 0x13) {
                        List<Long> longList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            longList.add(readLong());
                        }
                        field.set(obj, longList);
                    }
                }
            }else{
                if(readByte()==1) {
                    field.set(obj, readObject());
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

    public long readLong(){
        return decoder.readLong();
    }

    public short readShort(){
        return decoder.readShort();
    }

    public byte readByte(){
        return decoder.readByte();
    }


    public char readChar(){
        return decoder.readChar();
    }

    public float readFloat(){
        return decoder.readFloat();
    }

    public double readDouble(){
        return decoder.readDouble();
    }

    public boolean readBoolean(){
        return decoder.readBoolean();
    }

    public String readString(){
        return decoder.readString();
    }

    public Object readObjectValue() {
        return decoder.readObjectValue();
    }
}
