package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.util.FieldUtil;

import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * Created by linx on 2015/7/22.
 */
public class ObjectOutput {

    Encoder encoder=new ByteEncoder();
    ClassInfo classInfo;


    public ObjectOutput(OutputStream outputStream){

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
            String type=field.getType().getName();
            if("int".equals(type)){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }else if("long".equals(type)){
                 long value=field.getLong(obj);
                 encoder.writeLong(value);
            }else if("byte".equals(type)){
                byte value=field.getByte(obj);
                encoder.writeByte(value);
            }else if("short".equals(type)){
                short value=field.getShort(obj);
                encoder.writeShort(value);
            }else if("char".equals(type)){
                char value=field.getChar(obj);
                encoder.writeChar(value);
            }else if("float".equals(type)){
                float value=field.getFloat(obj);
                encoder.writeFloat(value);
            }else if("double".equals(type)){
                double value=field.getDouble(obj);
                encoder.writeDouble(value);
            }else if("boolean".equals(type)){
                boolean value=field.getBoolean(obj);
                encoder.writeBoolean(value);
            }
        }
    }

    public void writeObjectFields(Field[] fields,Object obj) throws IllegalAccessException {
        if(fields.length>0 && obj!=null){
            for (Field field: fields){
                field.setAccessible(true);
                String typeName=field.getType().getSimpleName();
                Object value=field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    this.writeObjectField(field, value, typeName);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }
        }
    }

    public void writeObjectField(Field field,Object value,String typeName) throws IllegalAccessException {

            if("Integer".equals(typeName)){
                    encoder.writeInt((Integer) value);
            }else if("Short".equals(typeName)){
                    encoder.writeShort((Short) value);
            }else if("Byte".equals(typeName)){
                    encoder.writeByte((Byte) value);
            }else if("Long".equals(typeName)){
                   encoder.writeLong((Long) value);
            }else if("ArrayList".equals(typeName)|| "List".equals(typeName)){
                    encoder.writeByte((byte) 0x99);
                    ArrayListSerializer listSerializer = new ArrayListSerializer();
                    listSerializer.writeObject(this, field, value, encoder);
            }else if("Character".equals(typeName)){
                    encoder.writeChar((Character) value);
            }else if("Float".equals(typeName)){
                    encoder.writeFloat((Float) value);
            }else if("Double".equals(typeName)){
                    encoder.writeDouble((Double) value);
            }else if("Boolean".equals(typeName)){
                    encoder.writeBoolean((Boolean) value);
            }else if("Object".equals(typeName)){
                    encoder.writeObject(value);
            }else if("String".equals(typeName)){
                    encoder.writeString((String) value);
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
