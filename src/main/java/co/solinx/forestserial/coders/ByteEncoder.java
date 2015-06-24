package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/6/19.
 */
public class ByteEncoder {


    public static void main(String[] args) {

        Test request = new Test();
        request.setA(10);
        request.setB(5);
        request.setCc(254);
        request.setBl(false);
        request.setBt((byte) 12);
        request.setCr('A');
        request.setSt((short) 27);
        request.setFl(12.3f);
        request.setDl(13.2);
        request.setBa("Ba");
        request.setBb("Bb");
        request.setInte(132);
        request.setLongNum(345l);
        request.setCharNum('B');
        request.setBooleanNum(true);
        request.setFloatNum(19.5f);
        request.setDoubleNum(20.1d);
        request.setShortNum((short) 21);
        request.setByteNum((byte) 29);
        request.setStringString("Stringing");
        request.setZzzz(9999999l);
        request.setObj(12);


        ByteEncoder encoder = new ByteEncoder();
        ByteDecoder decoder=new ByteDecoder();
        byte[] dataByte= encoder.encoder(request);
        Test temp= (Test) decoder.decoder(dataByte);

        System.out.println(temp.toString());
    }

    public byte[] encoder(Object obj) {
        FieldUtil fieldUtil = new FieldUtil();

        String className = this.getClassName(obj);
        byte[] clazz = className.getBytes();
        byte[] byteData = new byte[clazz.length+2];
        byteData[0] = 88;
        //类长度
        byteData[1] = (byte) clazz.length;
        //类全限定名
        System.arraycopy(clazz, 0, byteData, 2, clazz.length);
        //所有声明的字段字段
        Field[] fieldArray = obj.getClass().getDeclaredFields();


        //对字段按名称排序
        fieldArray = fieldUtil.fieldSort(fieldArray);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        byte[] primitiveByte= this.primitiveTypeToByte(primitiveFields, obj);
        byte[] objectByte= this.objectFieldTypeToByte(objectFields,obj);

        System.out.println("对象类型："+StringUtil.bytesToString(objectByte));
//        for (Field field: fieldArray){
//
//               System.out.println( field.getType().isPrimitive());
//        }

        System.out.println("类名："+StringUtil.bytesToString(byteData));
        System.out.println(fieldUtil.getFieldValue(fieldArray[0], obj));

        //存储基本类型与对象类型合并的结果
        byte[] result=new byte[primitiveByte.length+byteData.length+objectByte.length];
        System.arraycopy(byteData,0,result,0,byteData.length);
        //拷贝原生类型数据
        System.arraycopy(primitiveByte,0,result,byteData.length,primitiveByte.length);
        //拷贝对象类型数据
        System.arraycopy(objectByte,0,result,primitiveByte.length+byteData.length,objectByte.length);

        System.out.println(className);
        System.out.println("合并后："+StringUtil.bytesToString(result));

        return result;
    }

    /**
     * 编码对象类型
     * @param fields
     * @param obj
     * @return
     */
    public byte[] objectFieldTypeToByte(Field[] fields, Object obj) {

        ByteBuffer byteBuf=ByteBuffer.allocate(fields.length);
        byte[] fieldByte;
        for(Field field:fields){
            String typeName=field.getType().getSimpleName();
            field.setAccessible(true);
            System.out.println(typeName);
            try {
                if (typeName.equals("String")) {
                    String value= (String) field.get(obj);
                    byte[] valueBytes=new byte[value.getBytes().length+2];
                    valueBytes[0]=0x0f;
                    valueBytes[1]= (byte) value.getBytes().length;
                    System.arraycopy(value.getBytes(),0,valueBytes,2,value.getBytes().length);
                    byteBuf= this.put(byteBuf,valueBytes);

                } else if(typeName.equals("Integer")){
                    int value = (int) field.get(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Long")){
                    long value = (long) field.get(obj);
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Character")){
                    Character value= (Character) field.get(obj);
                    fieldByte=TypeToByteArray.charToByteArr(value);
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Short")){

                    Short value= (Short) field.get(obj);
                    fieldByte=TypeToByteArray.shortToByteArr(value);
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Byte")){
                    Byte value= (Byte) field.get(obj);
                    fieldByte=new byte[1];
                    fieldByte[0]=value;
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Float")){
                    Float value= (Float) field.get(obj);
                    fieldByte= TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Double")){
                    Double value= (Double) field.get(obj);
                    fieldByte=TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if(typeName.equals("Boolean")){
                    Boolean value= (Boolean) field.get(obj);
                    if(value){
                        fieldByte=TypeToByteArray.intToByteArr(1);
                    }else{
                        fieldByte=TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf=   this.put(byteBuf,fieldByte);
                }else if("Object".equals(typeName)){
                    Object value=field.get(obj);
                    fieldByte=value.toString().getBytes();
                    byte[] valueBytes=new byte[fieldByte.length+2];
                    valueBytes[0]=0x0f;
                    valueBytes[1]= (byte) fieldByte.length;
                    System.arraycopy(fieldByte,0,valueBytes,2,fieldByte.length);

                    System.out.println("Object："+value);
                    byteBuf=this.put(byteBuf,valueBytes);
                }else{
                    //ClassType
                    String className=field.getType().getTypeName();
                    this.classTypeToByte(className);
                    System.out.println(field.get(obj));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

       byte[] resultByte=new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(),0,resultByte,0,byteBuf.position());


        return resultByte;
    }

    public byte[] classTypeToByte(String className){

        return new byte[0];
    }

    /**
     * 原始类型转换为byte数组
     *
     * @param fields
     * @param obj
     * @return
     */
    public byte[] primitiveTypeToByte(Field[] fields, Object obj) {
        ByteBuffer byteBuf= ByteBuffer.allocate(fields.length * 8);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String typeName=field.getType().getName();
            field.setAccessible(true);
            byte[] fieldByte;
            try {

                if (typeName.equals("int")) {
                    int value = field.getInt(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("long")) {
                    long value =  field.getLong(obj);
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if(typeName.equals("boolean")){
                    boolean value=field.getBoolean(obj);
                    if(value){
                        fieldByte=TypeToByteArray.intToByteArr(1);
                    }else{
                        fieldByte=TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("char")){
                    char value= field.getChar(obj);
                    fieldByte=TypeToByteArray.charToByteArr(value);
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("float")){
                    float value=field.getFloat(obj);
                    fieldByte= TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("double")){
                    double value=field.getDouble(obj);
                    fieldByte=TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("short")){
                    short value=field.getShort(obj);
                    fieldByte=TypeToByteArray.shortToByteArr(value);
                    byteBuf.put(fieldByte);
                }else if(typeName.equals("byte")){
                    byte value=field.getByte(obj);
                    fieldByte=new byte[1];
                    fieldByte[0]=value;
                    byteBuf.put(fieldByte);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(StringUtil.bytesToString(primitiveByte));
        byte[] resultByte=new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(),0,resultByte,0,byteBuf.position());
        System.out.println(StringUtil.bytesToString(resultByte));
        return resultByte;
    }

    public static ByteBuffer put(ByteBuffer byteBuf,byte[] bytes){
        ByteBuffer tempBuf;
        //检查buffer剩余容量
        if(byteBuf.remaining()<bytes.length){
            //剩余容量不够，扩大bytes.length个长度
            tempBuf=ByteBuffer.allocate(byteBuf.array().length + bytes.length);
            byte[] curByte=new byte[byteBuf.position()];
            //把原本的数据拷贝到curByte中
            System.arraycopy(byteBuf.array(),0,curByte,0,byteBuf.position());

            tempBuf.put(curByte);
            tempBuf.put(bytes);
//            System.out.println("扩容");
//            System.out.println(StringUtil.bytesToString(bytes));
//            System.out.println(StringUtil.bytesToString(tempBuf.array()));
        }else{
            tempBuf=ByteBuffer.allocate(byteBuf.array().length);
            byte[] temp =new byte[byteBuf.position()];
            System.arraycopy(byteBuf.array(), 0, temp, 0, byteBuf.position());
            tempBuf.put(temp);
            tempBuf.put(bytes);
        }
//        byteBuf.put(bytes);
        return tempBuf;
    }


    public String getClassName(Object obj) {
        String className = obj.getClass().getName();

        return className;
    }


}
