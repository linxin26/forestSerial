package co.solinx.forestserial.serializer;

import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.coders.Encoder;
import co.solinx.forestserial.common.CodeType;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

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
          this.writeObjectHeader(obj);
         this.initClassInfo(obj);

       this.writeField(obj);

    }

   public void writeField(Object obj){

       Field[] fields = classInfo.getDeclaredFields();
       FieldUtil fieldUtil=new FieldUtil();
       fields=fieldUtil.fieldSort(fields);
       Field[] primitiveField= fieldUtil.getPrimitiveTypeField(fields);
       Field[] objectField=fieldUtil.getObjectTypeField(fields);

       this.writePrimitiveField(primitiveField,obj);
       this.writeObjectField(objectField,obj);
   }


    public void writeObjectHeader(Object obj){

             encoder.writeTag(CodeType.CLASS_NAME);
             encoder.writeClass(obj.getClass());
    }

    public void writePrimitiveField(Field[] fields,Object obj){
        for (Field field:fields){

        }
    }

    public void writeObjectField(Field[] fields,Object obj){
        for(Field field: fields){

        }
    }


    public void initClassInfo(Object clazz){
        classInfo=new ClassInfo(clazz);
        System.out.println(StringUtil.bytesToString(encoder.toByte()));
        System.out.println( classInfo.getDeclaredFields().length);
    }


}
