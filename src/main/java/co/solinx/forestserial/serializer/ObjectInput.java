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
        this.readObjectFields(objectField, obj);

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
        this.readObjectFields(objectField, obj);


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
                field.set(obj,readBoolean()) ;
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

    public void readObjectFields(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field:fields){
             field.setAccessible(true);
            readObjectField(field,obj);
        }
    }

    public Object instanceTagData(){
        Object value = null;
        byte tag=readByte();
        switch(tag){
            case ObjectOutput.INTEGER:
                value=readInt();
                break;
            case ObjectOutput.SHORT:
                value=readShort();
                break;
            case ObjectOutput.BYTE:
                value=readByte();
                break;
            case ObjectOutput.STRING:
                value=readString();
                break;
            case ObjectOutput.FLOAT:
                value=readFloat();
                break;
            case ObjectOutput.DOUBLE:
                value=readDouble();
                break;
            case ObjectOutput.LONG:
                value=readLong();
                break;
            case ObjectOutput.BOOLEAN:
                value=readBoolean();
                break;
            case ObjectOutput.CHAR:
                value=readChar();
                break;
            case ObjectOutput.LIST:
                ArrayListSerializer serializer = new ArrayListSerializer();
               value= serializer.instance(this);
                break;
            case ObjectOutput.MAP:
                MapSerializer mapSerializer = new MapSerializer();
               value= mapSerializer.instance(this);
                break;
        }
        return value;
    }

    public void readObjectField(Field field,Object obj) throws IllegalAccessException {
            Type type=field.getType();
          if(isNotNull()) {
              if (Integer.class == type&& readByte()==ObjectOutput.INTEGER) {
                      field.set(obj, readInt());
              } else if (Short.class == type&& readByte()== ObjectOutput.SHORT) {
                      field.set(obj, readShort());
              } else if (Long.class == type&& readByte()==ObjectOutput.LONG) {
                      field.set(obj, readLong());
              } else if (Byte.class == type&& readByte()==ObjectOutput.BYTE) {
                      field.set(obj, readByte());
              } else if (Character.class == type&& readByte()==ObjectOutput.CHAR) {
                      field.set(obj, readChar());
              } else if (Float.class == type&& readByte()==ObjectOutput.FLOAT) {
                      field.set(obj, readFloat());
              } else if (Double.class == type&& readByte()==ObjectOutput.DOUBLE) {
                      field.set(obj, readDouble());
              } else if (Boolean.class == type&& readByte()==ObjectOutput.BOOLEAN) {
                      field.set(obj, readBoolean());
              } else if (Object.class == type&& readByte()==ObjectOutput.OBJECT) {
                  System.out.println("--------------------------------------------");
                  System.out.println(field);
                      field.set(obj, readObjectValue());
              } else if (String.class == type&& readByte()==ObjectOutput.STRING) {
                      field.set(obj, readString());
              } else if ((List.class == type || ArrayList.class == type)&& readByte()==ObjectOutput.LIST) {
                      ArrayListSerializer serializer = new ArrayListSerializer();
                      field.set(obj, serializer.instance(this));
              } else if (Map.class == type&& readByte()==ObjectOutput.MAP) {
                      MapSerializer serializer = new MapSerializer();
                      field.set(obj, serializer.instance(this));
              }else if(field.getType().isEnum()&&readByte()==ObjectOutput.ENUM){
                  try {
                      field.set(obj, readEnum());
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              } else {
                      field.set(obj, readObject());
              }
          }
    }

    public Object readEnum() throws ClassNotFoundException {
        Class clazz= Class.forName(readString());
        int ordinal= readInt();
        Object[] enumConstants= clazz.getEnumConstants();
        return enumConstants[ordinal];
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
