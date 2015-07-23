package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.CodeType;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
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
          this.writeObjectHeader(obj);
         this.initClassInfo(obj);

       this.writeField(obj);

        System.out.println(StringUtil.bytesToString(encoder.toByte()));
    }

   public void writeField(Object obj){

       Field[] fields = classInfo.getDeclaredFields();
       FieldUtil fieldUtil=new FieldUtil();
       fields=fieldUtil.fieldSort(fields);
       Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
       Field[] objectField=fieldUtil.getObjectTypeField(fields);

       try {

           this.writePrimitiveField(primitiveField, obj);
           this.writeObjectField(objectField, obj);
       }catch(Exception e){
           e.printStackTrace();
       }
   }


    public void writeObjectHeader(Object obj){

             encoder.writeTag(CodeType.CLASS_NAME);
             encoder.writeClass(obj.getClass());
    }

    public void writePrimitiveField(Field[] fields,Object obj) throws IllegalAccessException {
        for (Field field:fields){
            field.setAccessible(true);
            if("int".equals(field.getType().getName())){
                int value=field.getInt(obj);
                encoder.writeInt(value);
                System.out.println("int value "+value);
            }
        }
    }

    public void writeObjectField(Field[] fields,Object obj) throws IllegalAccessException {
        for(Field field: fields){
            field.setAccessible(true);
            if("ArrayList".equals(field.getType().getSimpleName())|| "List".equals(field.getType().getSimpleName())){

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
                System.out.println(field.getType().getName());
            }

        }
    }


    public void initClassInfo(Object clazz){
        classInfo=new ClassInfo(clazz);
        System.out.println(StringUtil.bytesToString(encoder.toByte()));
        System.out.println( classInfo.getDeclaredFields().length);
    }

    public byte[] toBytes(){
        return encoder.toByte();
    }

}
