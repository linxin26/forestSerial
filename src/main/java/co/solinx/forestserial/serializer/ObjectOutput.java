package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.util.FieldUtil;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by linx on 2015/7/22.
 */
public class ObjectOutput {

    Encoder encoder=new ByteEncoder();
    ClassInfo classInfo;
    public static final byte BYTE=0x12;
    public static final byte INTEGER=0x10;
    public static final byte SHORT=0x11;
    public static final byte LONG=0x13;
    public static final byte CHAR=0x14;
    public static final byte FLOAT=0x15;
    public static final byte DOUBLE=0x16;
    public static final byte STRING=0x19;
    public static final byte OBJECT=0x18;
    public static final byte LIST=0x20;
    public static final byte MAP=0x21;
    public static final byte BOOLEAN=0x17;
    public static final byte ENUM=0x22;
    private SerializeContext serializeContext=new SerializeContext();


    public ObjectOutput(){

    }

    public void writeObject(Object obj){
        this.writeObjectHeader(obj.getClass());
        this.initClassInfo(obj);

        this.writeField(obj);

    }

   public void writeField(Object obj){

       Field[] fields=obj.getClass().getDeclaredFields();
       FieldUtil fieldUtil=new FieldUtil();
       fields=fieldUtil.fieldSort(fields);
       Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
       Field[] objectField=fieldUtil.getObjectTypeField(fields);

       try {

           this.writePrimitiveField(primitiveField, obj);
           this.writeObjectFields(objectField, obj);
           writeSuperClass(obj, obj.getClass().getSuperclass());
       }catch(Exception e){
           e.printStackTrace();
       }
   }

    public void writeObject(Object obj ,Class clazz){
        this.writeObjectHeader(clazz);
        this.initClassInfo(obj);

        Field[] fields=clazz.getDeclaredFields();
        FieldUtil fieldUtil=new FieldUtil();
        fields=fieldUtil.fieldSort(fields);
        Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
        Field[] objectField=fieldUtil.getObjectTypeField(fields);

        try{
            this.writePrimitiveField(primitiveField, obj);
            this.writeObjectFields(objectField, obj);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void writeSuperClass(Object obj,Class superClass){
        System.out.println("class ：" + obj.getClass().getName());
        System.out.println("superClass ：" + superClass.getName());
//        Class superClass=obj.getClass().getSuperclass();
        if(!"Object".equals(superClass.getSimpleName())) {
            writeSuperClass(obj,superClass.getSuperclass());
            writeObject(obj, superClass);
        } else {
            System.out.println("exit ：" + superClass.getName());
        }
    }

    public void writeObjectHeader(Class clazz){
        encoder.writeTag(DataType.CLASS_NAME);
        encoder.writeClass(clazz);
    }

    public void writePrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field:fields){
            field.setAccessible(true);
            Type type=field.getType();
            if(Integer.TYPE==type){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }else if(Long.TYPE==type){
                 long value=field.getLong(obj);
                 encoder.writeLong(value);
            }else if(Byte.TYPE==type){
                byte value=field.getByte(obj);
                encoder.writeByte(value);
            }else if(Short.TYPE==type){
                short value=field.getShort(obj);
                encoder.writeShort(value);
            }else if(Character.TYPE==type){
                char value=field.getChar(obj);
                encoder.writeChar(value);
            }else if(Float.TYPE==type){
                float value=field.getFloat(obj);
                encoder.writeFloat(value);
            }else if(Double.TYPE==type){
                double value=field.getDouble(obj);
                encoder.writeDouble(value);
            }else if(Boolean.TYPE==type){
                boolean value=field.getBoolean(obj);
                encoder.writeBoolean(value);
            }
        }
    }

    public void writeObjectFields(Field[] fields,Object obj) throws Exception {
        if(fields.length>0 && obj!=null){
            for (Field field: fields){
                field.setAccessible(true);
                Object value=field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    this.writeObjectField(field, value, field.getType());
                }else{
                    encoder.writeByte((byte) 0);
                }
            }
        }
    }

    public void writeTag(byte tag){
        encoder.writeByte(tag);
    }

    public void writeObjectField(Field field,Object value,Class typeName) throws Exception {
            if(Integer.class==typeName){
                   writeTag(INTEGER);
                    encoder.writeInt((Integer) value);
            }else if(Short.class==typeName){
                    writeTag(SHORT);
                    encoder.writeShort((Short) value);
            }else if(Byte.class==typeName){
                    writeTag(BYTE);
                    encoder.writeByte((Byte) value);
            }else if(Long.class==typeName){
                    writeTag(LONG);
                   encoder.writeLong((Long) value);
            }else if(ArrayList.class==typeName|| List.class==typeName){
                    writeTag(LIST);
                serializeContext.setSerializer(new ArrayListSerializer());
                serializeContext.writeObject(this, field, value, encoder);
            }else if(Character.class==typeName){
                  writeTag(CHAR);
                    encoder.writeChar((Character) value);
            }else if(Float.class==typeName){
                    writeTag(FLOAT);
                    encoder.writeFloat((Float) value);
            }else if(Double.class==typeName){
                   writeTag(DOUBLE);
                    encoder.writeDouble((Double) value);
            }else if(Boolean.class==typeName){
                    writeTag(BOOLEAN);
                    encoder.writeBoolean((Boolean) value);
            }else if(Object.class==typeName){
                    writeTag(OBJECT);
                    encoder.writeObject(value);
            }else if(String.class==typeName){
                   writeTag(STRING);
                    encoder.writeString((String) value);
            }else if(Map.class==typeName|| HashMap.class==typeName){
                   writeTag(MAP);
                serializeContext.setSerializer(new MapSerializer());
                serializeContext.writeObject(this, field, value, encoder);
            }else if(field.getType().isEnum()){
                writeTag(ENUM);
                encoder.writeString(value.getClass().getName());
                encoder.writeInt(((Enum)value).ordinal());
            }else if(typeName.isArray()){

            }else{
                        writeObject(value);
            }
    }



    public void initClassInfo(Object clazz){
//        classInfo=new ClassInfo(clazz);
    }

    public byte[] toBytes(){
        return encoder.toByte();
    }

}
