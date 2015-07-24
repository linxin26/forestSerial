package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.CodeType;
import co.solinx.forestserial.util.FieldUtil;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

           this.writePrimitiveField(primitiveField, obj);
           this.writeObjectField(objectField, obj);
           writeSuperClass(obj);
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

        for (Field field: primitiveField){
            System.out.println(field);
        }

        try {

            this.writePrimitiveField(primitiveField, obj);
            this.writeObjectField(objectField, obj);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void writeSuperClass(Object obj){
        System.out.println(obj.getClass().getSuperclass().getName());
        writeObject(obj, obj.getClass().getSuperclass());
    }

    public void writeObjectHeader(Class clazz){
        encoder.writeTag(CodeType.CLASS_NAME);
        encoder.writeClass(clazz);
    }

    public void writePrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field:fields){
            field.setAccessible(true);
            if("int".equals(field.getType().getName())){
                int value=field.getInt(obj);
                encoder.writeInt(value);
            }else if("long".equals(field.getType().getName())){
                 long value=field.getLong(obj);
                 encoder.writeLong(value);
            }else if("byte".equals(field.getType().getName())){
                byte value=field.getByte(obj);
                encoder.writeByte(value);
            }else if("short".equals(field.getType().getName())){
                short value=field.getShort(obj);
                encoder.writeShort(value);
            }
        }
    }

    public void writeObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            String typeName=field.getType().getSimpleName();
            if("Integer".equals(typeName)){
                Integer value= (Integer) field.get(obj);
                encoder.writeInt(value);
            }else if("Short".equals(typeName)){
                Short value= (Short) field.get(obj);
                encoder.writeShort(value);
            }else if("Byte".equals(typeName)){
                Byte value= (Byte) field.get(obj);
                encoder.writeByte(value);
            }else if("Long".equals(typeName)){
                Long value= (Long) field.get(obj);
               if(value!=null) {
                   encoder.writeLong(value);
               }else{
                   encoder.writeLong(0);
               }
            }else if("ArrayList".equals(typeName)|| "List".equals(typeName)){
                  Object value= field.get(obj);
                if(value!=null) {
                    Type type=field.getGenericType();
                    if( type instanceof ParameterizedType){
                        Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];
                        if ("java.lang.Integer".equals(clazz.getTypeName())) {
                            List<Integer> valueList = (List<Integer>) field.get(obj);
                            encoder.writeByte((byte) 0x99);
                            encoder.writeByte((byte) 0x11);
                            encoder.writeInt(valueList.size());
                            for (Integer temp : valueList) {
                                encoder.writeInt(temp);
                            }
                        }else if("java.lang.String".equals(clazz.getTypeName())){
                            List<String> valueList = (List<String>) field.get(obj);
                            encoder.writeByte((byte) 0x99);
                            encoder.writeByte((byte) 0x12);
                            encoder.writeInt(valueList.size());
                            for (String temp : valueList) {
                                encoder.writeString(temp);
                            }
                        }
                    }
                }
            }else if("Character".equals(typeName)){

            }else if("Float".equals(typeName)){

            }else if("Double".equals(typeName)){

            }else if("Boolean".equals(typeName)){

            }else if("Object".equals(typeName)){

            }else if("String".equals(typeName)){

            }else{
                  Object value=field.get(obj);
                  if(value!=null){
                      writeObject(value);
                  }
            }

        }
    }


    public void initClassInfo(Object clazz){
//        classInfo=new ClassInfo(clazz);
    }

    public byte[] toBytes(){
        return encoder.toByte();
    }

}
