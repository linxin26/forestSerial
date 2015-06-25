package co.solinx.forestserial.coders;

import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by linx on 2015-06-22.
 */
public class ByteDecoder {

    public Object decoder(byte[] byteData) {

        System.out.println("-------------------------------decoder-------------------------");
        int flag = byteData[0];   //标志位
        int claLength = byteData[1];   //类名长度
        byte[] claNameByte = new byte[claLength];
        System.arraycopy(byteData, 2, claNameByte, 0, claLength);
        String claName = new String(claNameByte);

        byte[] fieldByte=new byte[byteData.length-(claLength+2)];
        System.arraycopy(byteData,claLength+2,fieldByte,0,fieldByte.length);
        System.out.println(claName);

        Object clazz= this.getClazzInstance(claName);
        FieldUtil fieldUtil=new FieldUtil();
        Field[] fieldArray=fieldUtil.fieldSort(clazz.getClass().getDeclaredFields());

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields=fieldUtil.getObjectTypeField(fieldArray);

        ByteBuffer byteBuf=ByteBuffer.wrap(fieldByte);
        this.dePrimitive(clazz,primitiveFields,byteBuf);
        this.deObject(clazz,objectFields,byteBuf);
        this.superClassToByte(clazz,clazz.getClass().getSuperclass(),byteBuf);
        System.out.println(clazz);
        return clazz;
    }

    /**
     * 父类转为
     * @param obj
     * @param superClass
     * @return
     */
    public byte[] superClassToByte(Object obj,Class superClass,ByteBuffer byteBuffer){
        System.out.println("--------------superClassToByte-----------------");
        System.out.println(superClass.getName());
        String className=superClass.getSimpleName();
        byte[]  superByte=new byte[0];
        if (className!="object"){
            Field[] fields= superClass.getDeclaredFields();
         this.fieldToByte(obj,fields,byteBuffer);
        }

        return superByte;
    }

    /**
     * Field转为Byte
     * @param obj
     * @param fields
     * @return
     */
    public void fieldToByte(Object obj,Field[] fields,ByteBuffer byteBuf) {

        FieldUtil fieldUtil =new FieldUtil();
        Field[]  fieldArray= fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        this.dePrimitive(obj,primitiveFields,byteBuf);
        this.deObject(obj,objectFields,byteBuf);

    }

    public void deObject(Object obj,Field[] fields,ByteBuffer byteBuf){
//        System.out.println("deObject : "+StringUtil.bytesToString(byteBuf.array()));
//        System.out.println("position : "+byteBuf.position());
        for (Field field:fields){
            String typeName=field.getType().getSimpleName();
//            System.out.println(field);
            field.setAccessible(true);
            try {
                if ("String".equals(typeName)) {
//                    System.out.println("String ");
                    byteBuf.get();  //类型0f
                    int length = byteBuf.get();
                    byte[] valueByte = new byte[length];
                    byteBuf.get(valueByte);
                    String value=new String(valueByte);
                    field.set(obj, value);
                }else if ("Integer".equals(typeName)){
                    int value=byteBuf.getInt();
                    field.set(obj, value);
                }else if("Long".equals(typeName)){
                    long value=byteBuf.getLong();
                    field.set(obj, value);
                }else if("Float".equals(typeName)){
                    float value=byteBuf.getFloat();
                    field.set(obj,value);
                }else if("Double".equals(typeName)){
                    double value=byteBuf.getDouble();
                    field.set(obj,value);
                }else if("Character".equals(typeName)){
                    char value=byteBuf.getChar();
                    field.set(obj,value);
                }else if("Short".equals(typeName)){
                    short value=byteBuf.getShort();
                    field.set(obj,value);
                }else if("Boolean".equals(typeName)){
                    int value=byteBuf.getInt();
                    boolean result;
                    if (value==1){
                        result=true;
                    }else{
                        result=true;
                    }
                    field.set(obj,result);
                }else if("Byte".equals(typeName)){
                    byte value=byteBuf.get();
                    field.set(obj,value);
                }else if("Object".equals(typeName)){
                   if(byteBuf.hasRemaining()) {
                       byteBuf.get();  //类型0f
                       int length = byteBuf.get();
                       byte[] valueByte = new byte[length];
                       byteBuf.get(valueByte);
                       String value = new String(valueByte);
                       field.set(obj, value);
                   }
                }else{
                    //类类型
                    System.out.println("Field : "+field.getType().getName());
                    String className=field.getType().getName();
                    FieldUtil fieldUtil=new FieldUtil();
                     Object clazz= Class.forName(className).newInstance();

                    byte type=byteBuf.get();
                    byte length=byteBuf.get();
                    byte[] data=new byte[length];
                    byteBuf.get(data);

                    Field[] fieldArray=fieldUtil.fieldSort(clazz.getClass().getDeclaredFields());

                    Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
                    Field[] objectFields=fieldUtil.getObjectTypeField(fieldArray);

                    for (Field tempField: primitiveFields){
                        System.out.println("primitiveFields : "+tempField);
                    }
                    System.out.println("data : "+StringUtil.bytesToString(data));
                    ByteBuffer tempBuf=ByteBuffer.wrap(data);
                    this.dePrimitive(clazz,primitiveFields,tempBuf);
                    this.deObject(clazz,objectFields,tempBuf);

                    System.out.println("classType : "+type);
                    field.set(obj,clazz);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 解码原始数据类型
     * @param obj 对象
     * @param fields 字段集合
     * @param byteBuf Buffer
     */
    public void dePrimitive(Object obj,Field[] fields,ByteBuffer byteBuf){
        int index=0;
        try {
        for (Field field:fields){
            field.setAccessible(true);
            String type= field.getType().getName();
            if(type.equals("int")) {
                    field.setInt(obj, byteBuf.getInt());
                index=index+4;
            }else if(type.equals("long")){
                field.setLong(obj, byteBuf.getLong());
                index=index+8;
            }else if(type.equals("float")){
                byte[] iByte=new byte[4];
                byteBuf.get(iByte);
                int value= TypeToByteArray.hBytesToInt(iByte);
                field.setFloat(obj,Float.intBitsToFloat( value));
                index=index+4;
            }else if(type.equals("double")){
                byte[] iByte=new byte[8];
                byteBuf.get(iByte);
                long value= TypeToByteArray.getLong(iByte);
                field.setDouble(obj, Double.longBitsToDouble(value));
                index=index+8;
            }else if(type.equals("char")){
                field.setChar(obj, byteBuf.getChar());
                index=index+2;
            }else if(type.equals("boolean")){
                byte[] iByte=new byte[4];
                byteBuf.get(iByte);
                int value= TypeToByteArray.hBytesToInt(iByte);
                boolean result;
                if(value==1){
                    result=true;
                }else{
                    result=false;
                }
                field.setBoolean(obj,result);
                index=index+4;
            }else if(type.equals("short")){
                field.setShort(obj, byteBuf.getShort());
                index=index+2;
            }else if(type.equals("byte")){
                field.setByte(obj, byteBuf.get());
                index=index+1;
            }else{
                index=index+4;
            }
        }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getClazzInstance(String clazzName) {

        Object instance = null;
        try {
            Class cla  = Class.forName(clazzName);
              instance= cla.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
