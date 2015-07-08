package co.solinx.forestserial.serializer;

import co.solinx.forestserial.Buffer.BufferStream;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/7/7.
 */
public class DeFieldInfo {

    Object obj;


    public void deField(Object obj,Field[] fields,BufferStream bufferStream){
        this.obj=obj;

        FieldUtil fieldUtil=new FieldUtil();

        Field[]  fieldArray= fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);


        this.dePrimitiveField(obj, primitiveFields, bufferStream);
        this.deObjectField(obj, objectFields, bufferStream);

    }

    public void deObjectField(Object obj,Field[] fields,BufferStream bufferStream){
        System.out.println("start position : " +bufferStream.getPosition());
        for (Field field:fields){

            System.out.println(field);
            String typeName=field.getType().getSimpleName();
            field.setAccessible(true);
            try {
                if ("String".equals(typeName)) {

//                    System.out.println(typeName);

                    bufferStream.getByte();  //类型0f
                    int length = bufferStream.getByte();

                    byte[] valueByte = new byte[length];
                    bufferStream.getByte(valueByte);
                    String value=new String(valueByte);
//                    System.out.println("Length : "+length);
//                    System.out.println("value : "+value);
                    field.set(obj, value);
                }else if ("Integer".equals(typeName)){
                    int value=bufferStream.getInt();
                    field.set(obj, value);
                }else if("Long".equals(typeName)){
                    long value=bufferStream.getLong();
                    field.set(obj, value);
                }else if("Float".equals(typeName)){
                    float value=bufferStream.getFloat();
                    field.set(obj,value);
                }else if("Double".equals(typeName)){
                    double value=bufferStream.getDouble();
                    field.set(obj,value);
                }else if("Character".equals(typeName)){
                    char value=bufferStream.getChar();
                    field.set(obj,value);
                }else if("Short".equals(typeName)){
                    short value=bufferStream.getShort();
                    field.set(obj,value);
                }else if("Boolean".equals(typeName)){
                    int value=bufferStream.getInt();
                    boolean result;
                    if (value==1){
                        result=true;
                    }else{
                        result=false;
                    }
                    field.set(obj,result);
                }else if("Byte".equals(typeName)){
                    byte value=bufferStream.getByte();
                    field.set(obj,value);
                }else if("Object".equals(typeName)){
                    if(bufferStream.hasRemaining()) {
                        bufferStream.getByte();  //类型0f
                        int length = bufferStream.getByte();
                        byte[] valueByte = new byte[length];
                        bufferStream.getByte(valueByte);

                        String value = new String(valueByte);
                        field.set(obj, value);
                    }
                }else if("List".equals(typeName)){
//                    System.out.println("-----------------------------------List "+byteBuf.getInt());
                    if (bufferStream.getByte()!=0) {
                        List list = new ArrayList<Integer>();
                        list.add(bufferStream.getInt());
                        field.set(obj, list);
                    }
                }else{
                    //类类型
                    String className=field.getType().getName();
                    Object clazz= Class.forName(className).newInstance();

                    byte type=bufferStream.getByte();
                    byte length=bufferStream.getByte();
                    if(length>0) {
                        byte[] data = new byte[length];
                        bufferStream.getByte(data);
                        Field[] fieldArray = clazz.getClass().getDeclaredFields();
                        ByteBuffer tempBuf = ByteBuffer.wrap(data);
                        BufferStream byteBuf=new BufferStream(data);
                        System.out.println(StringUtil.bytesToString(data));
                        //解码字段
                        this.deField(clazz, fieldArray, byteBuf);
                        //todo 类类型父类解码
                        this.superClassToByte(clazz, clazz.getClass().getSuperclass(), bufferStream);
                        field.set(obj, clazz);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 父类转为
     * @param obj 对象
     * @param superClass 父类
     */
    public void superClassToByte(Object obj,Class superClass,BufferStream byteBuffer) throws IllegalAccessException {
        String className=superClass.getSimpleName();
        if (!"Object".equals(className)){
            Field[] fields= superClass.getDeclaredFields();

//            for (Field field : fields){
//                System.out.println(field);
//            }
            this.deField(obj, fields, byteBuffer);
            System.out.println(" defieldInfo currentClass ： "+superClass + "  superClass.getSuperclass： "+superClass.getSuperclass());
            this.superClassToByte(obj,superClass.getSuperclass(),byteBuffer);
        }
    }


    public void dePrimitiveField(Object obj,Field[] fields,BufferStream bufferStream){
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String type = field.getType().getName();
                if (type.equals("int")) {
                    int value = bufferStream.getInt();
                    field.setInt(obj, value);
                } else if (type.equals("long")) {
                    field.setLong(obj, bufferStream.getLong());
                } else if (type.equals("float")) {
                    byte[] iByte = new byte[4];
                    bufferStream.getByte(iByte);
                    int value = TypeToByteArray.hBytesToInt(iByte);
                    field.setFloat(obj, Float.intBitsToFloat(value));
                } else if (type.equals("double")) {
                    byte[] iByte = new byte[8];
                    bufferStream.getByte(iByte);
                    long value = TypeToByteArray.getLong(iByte);
                    field.setDouble(obj, Double.longBitsToDouble(value));
                } else if (type.equals("char")) {
                    field.setChar(obj, bufferStream.getChar());
                } else if (type.equals("boolean")) {
                    byte[] iByte = new byte[4];
                    bufferStream.getByte(iByte);
                    int value = TypeToByteArray.hBytesToInt(iByte);
                    boolean result;
                    if (value == 1) {
                        result = true;
                    } else {
                        result = false;
                    }
                    field.setBoolean(obj, result);
                } else if (type.equals("short")) {
                    field.setShort(obj, bufferStream.getShort());
                } else if (type.equals("byte")) {
                    field.setByte(obj, bufferStream.getByte());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
