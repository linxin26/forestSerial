package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.util.FieldUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

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
            Type type=field.getType();
            if(Integer.TYPE==type) {
                field.set(obj, readInt());
            }else if(Long.TYPE==type){
                field.set(obj,readLong());
            }else if(Short.TYPE==type){
                field.set(obj,readShort());
            }else if(Byte.TYPE==type){
                field.set(obj,readByte());
            }else if(Character.TYPE==type){
                field.set(obj,readChar());
            }else if(Float.TYPE==type){
                field.set(obj,readFloat());
            }else if(Double.TYPE==type){
                field.set(obj,readDouble());
            }else if(Boolean.TYPE==type){
                field.set(obj,readBoolean());
            }
        }
    }

    public boolean isNotNull(){
        boolean val=false;
        if(readByte()==1){
            val=true;
        }
        return  val;
    }

    public void readObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field: fields){
            field.setAccessible(true);
            Type type=field.getType();
            if(Integer.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readInt());
                }
            }else if(Short.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readShort());
                }
            }else if(Long.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readLong());
                }
            }else if(Byte.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readByte());
                }
            }else if(Character.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readChar());
                }
            }else if(Float.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readFloat());
                }
            }else if(Double.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readDouble());
                }
            }else if(Boolean.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readBoolean());
                }
            }else if(Object.class==type){
                System.out.println("--------------------------------------------");
                System.out.println(field);
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readObjectValue());
                }
            }else if(String.class==type){
                if(isNotNull()) {
                    readByte();
                    field.set(obj, readString());
                }
            }else if(List.class==type|| ArrayList.class==type){
                if(isNotNull()) {
                    byte flag = readByte();
                    int typeItem = readByte();
                    int size = readInt();
                    if (typeItem == 0x11) {
                        List<Integer> integerList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            readByte();
                            integerList.add(readInt());
                        }
                        field.set(obj, integerList);
                    } else if (typeItem == 0x12) {
                        List<String> integerList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            readByte();
                            integerList.add(readString(readInt()));
                        }
                        field.set(obj, integerList);
                    }else if (typeItem == 0x13) {
                        List<Long> longList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            readByte();
                            longList.add(readLong());
                        }
                        field.set(obj, longList);
                    }else if(typeItem==0x14){
                        List<Character> charList=new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            readByte();
                            charList.add(readChar());
                        }
                        field.set(obj, charList);
                    }
                }
            }else if(Map.class==type){
                if(isNotNull()){
                    readByte();
                    Map map= new HashMap<>();
                    int size=readInt();
                    for (int i=0;i<size;i++){
                        Object key = null,value = null;
                        byte keyType=readByte();
                        if(keyType==0x19){
                            key=readString();
                        }else if(keyType==0x10){
                            key=readInt();
                        }else if(keyType==0x11){
                            key=readShort();
                        }
                        byte valueType=readByte();
                        if(valueType==0x10){
                            value=readInt();
                        }else if(valueType==0x13){

                        }else if(valueType==0x11){
                            value=readShort();
                        }
                         map.put(key,value);
                    }
                    field.set(obj,map);
                }
            }else{
                if(isNotNull()) {
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
