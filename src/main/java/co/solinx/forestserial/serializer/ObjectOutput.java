package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.DataType;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

//           for (Field field: primitiveField) {
//               field.setAccessible(true);
//               String type=field.getType().getSimpleName();
//               Object value= field.get(obj);
//               writeFields(value,type);
//           }

           this.writePrimitiveField(primitiveField, obj);
           this.writeObjectField(objectField, obj);
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
            this.writeObjectField(objectField, obj);

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

    public void writeObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            String typeName=field.getType().getSimpleName();
            if("Integer".equals(typeName)){
                Integer value= (Integer) field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    encoder.writeInt(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Short".equals(typeName)){
                Short value= (Short) field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    encoder.writeShort(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Byte".equals(typeName)){
                Byte value= (Byte) field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    encoder.writeByte(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Long".equals(typeName)){
                Long value= (Long) field.get(obj);
               if(value!=null) {
                   encoder.writeByte((byte) 1);
                   encoder.writeLong(value);
               }else{
                   encoder.writeByte((byte) 0);
               }
            }else if("ArrayList".equals(typeName)|| "List".equals(typeName)){
                  Object value= field.get(obj);

                  writeList(field, value, obj);
            }else if("Character".equals(typeName)){
                Character value= (Character) field.get(obj);
                if (value!=null) {
                    encoder.writeByte((byte) 1);
                    encoder.writeChar(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Float".equals(typeName)){
                  Float value= (Float) field.get(obj);
                if(value!=null) {
                    encoder.writeByte((byte) 1);
                    encoder.writeFloat(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Double".equals(typeName)){
                Double value= (Double) field.get(obj);
                if(value!=null){
                    encoder.writeByte((byte) 1);
                    encoder.writeDouble(value);
                }else{
                    encoder.writeByte((byte) 0);
                }

            }else if("Boolean".equals(typeName)){
                Boolean value= (Boolean) field.get(obj);
                if(value!=null){
                    encoder.writeByte((byte) 1);
                    encoder.writeBoolean(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else if("Object".equals(typeName)){
                Object value=field.get(obj);
                if(value!=null){
                    encoder.writeByte((byte) 1);
                    encoder.writeObject(value);
                }else{
                    encoder.writeByte((byte) 0);
                }

            }else if("String".equals(typeName)){
                String value= (String) field.get(obj);
                if(value!=null){
                    encoder.writeByte((byte) 1);
                    encoder.writeString(value);
                }else{
                    encoder.writeByte((byte) 0);
                }
            }else{
                  Object value=field.get(obj);
                  if(value!=null){
                      encoder.writeByte((byte) 1);
                      writeObject(value);
                  }else{
                      encoder.writeByte((byte) 0);
                  }
            }

        }
    }

    public void writeFields(Object value,String type,int size){
        System.out.println(value);
        System.out.println(type);
        if("java.lang.String".equals(type)){
            encoder.writeString((String) value);
        }else if("java.lang.Integer".equals(type)){
            encoder.writeInt((Integer) value);
        }else if("java.lang.Long".equals(type)){
            encoder.writeLong((Long) value);
        }
    }

    public void writeList(Field field,Object value,Object obj) throws IllegalAccessException {
        if(value!=null) {
            System.out.println("+++++++++++++++++++++++  ");
            ArrayList list= (ArrayList) value;
            encoder.writeByte((byte) 1);
            encoder.writeByte((byte) 0x99);
            Type type=field.getGenericType();
            if( type instanceof ParameterizedType){
                Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
                System.out.println("------------------------------"+clazz);
                if ("java.lang.Integer".equals(clazz.getTypeName())) {
                    encoder.writeByte((byte) 0x11);
                }else if("java.lang.String".equals(clazz.getTypeName())){
                    encoder.writeByte((byte) 0x12);
                }else if("java.lang.Long".equals(clazz.getTypeName())){
                    encoder.writeByte((byte) 0x13);
                }
            }
            encoder.writeInt(list.size());

            for (Object temp:list){
                 writeFields(temp,temp.getClass().getTypeName(),list.size());
//                 System.out.println(temp.getClass());
             }


//            Type type=field.getGenericType();
//            if( type instanceof ParameterizedType){
//                Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
//                System.out.println("------------------------------"+clazz);
//                if ("java.lang.Integer".equals(clazz.getTypeName())) {
//                    List<Integer> valueList = (List<Integer>) field.get(obj);
//                    encoder.writeByte((byte) 0x99);
//                    encoder.writeByte((byte) 0x11);
//                    encoder.writeInt(valueList.size());
//                    for (Integer temp : valueList) {
//                        encoder.writeInt(temp);
//                    }
//                }else if("java.lang.String".equals(clazz.getTypeName())){
//                    List<String> valueList = (List<String>) field.get(obj);
//                    encoder.writeByte((byte) 0x99);
//                    encoder.writeByte((byte) 0x12);
//                    encoder.writeInt(valueList.size());
//                    for (String temp : valueList) {
//                        encoder.writeString(temp);
//                    }
//                }
//            }
        }else {
            encoder.writeByte((byte) 0);
        }
    }


    public void initClassInfo(Object clazz){
//        classInfo=new ClassInfo(clazz);
    }

    public byte[] toBytes(){
        return encoder.toByte();
    }

}
