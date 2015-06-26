package co.solinx.forestserial.coders;

import co.solinx.forestserial.data.Response;
import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/6/19.
 *
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
        Response response=new Response();
        response.setResult("ppp");
        response.setSn(998);
        response.setBaseID(188);
        request.setResponse(response);
        request.setBaseClassID(190000);
        request.setResult("result");
        request.setSuperSn(18880);
        request.setBaseID(199);

        ByteEncoder encoder = new ByteEncoder();
        ByteDecoder decoder = new ByteDecoder();
        byte[] dataByte = encoder.encoder(request);
        Test temp = (Test) decoder.decoder(dataByte);

        System.out.println(temp.toString());
    }

    public static ByteBuffer put(ByteBuffer byteBuf, byte[] bytes) {
        ByteBuffer tempBuf;
        //检查buffer剩余容量
        if (byteBuf.remaining() < bytes.length) {
            //剩余容量不够，扩大bytes.length个长度
            tempBuf = ByteBuffer.allocate(byteBuf.array().length + bytes.length);
            byte[] curByte = new byte[byteBuf.position()];
            //把原本的数据拷贝到curByte中
            System.arraycopy(byteBuf.array(), 0, curByte, 0, byteBuf.position());

            tempBuf.put(curByte);
            tempBuf.put(bytes);
//            System.out.println("扩容");
//            System.out.println(StringUtil.bytesToString(bytes));
//            System.out.println(StringUtil.bytesToString(tempBuf.array()));
        } else {
            tempBuf = ByteBuffer.allocate(byteBuf.array().length);
            byte[] temp = new byte[byteBuf.position()];
            System.arraycopy(byteBuf.array(), 0, temp, 0, byteBuf.position());
            tempBuf.put(temp);
            tempBuf.put(bytes);
        }
//        byteBuf.put(bytes);
        return tempBuf;
    }

    /**
     * 对象转码为Byte[]
     *
     * @param obj
     * @return
     */
    public byte[] encoder(Object obj) {
        /**
         * Byte[]=标志+类名长度+原始类型+引用类型+父类
         * 每个引用类型
         */

        FieldUtil fieldUtil = new FieldUtil();

        String className = this.getClassName(obj);
        byte[] clazz = className.getBytes();
        byte[] byteData = new byte[clazz.length + 2];
        byteData[0] = 88;
        //类长度
        byteData[1] = (byte) clazz.length;
        //类全限定名
        System.arraycopy(clazz, 0, byteData, 2, clazz.length);
        //所有声明的字段字段
        Field[] fieldArray = obj.getClass().getDeclaredFields();

        //java
        String superClass=obj.getClass().getSuperclass().getSimpleName();
        System.out.println("superClass : "+superClass);
        byte[] superByte= this.superClassToByte(obj,obj.getClass().getSuperclass());

        //对字段按名称排序
        fieldArray = fieldUtil.fieldSort(fieldArray);

        byte[] fieldByte= this.fieldToByte(obj,fieldArray);


        System.out.println("类名：" + StringUtil.bytesToString(byteData));

        ByteBuffer fieldBuf=ByteBuffer.allocate(fieldByte.length+superByte.length+byteData.length);
        fieldBuf.put(byteData);
        fieldBuf.put(fieldByte);
        fieldBuf.put(superByte);
        System.out.println("合并后：" + StringUtil.bytesToString(fieldBuf.array()));
        return fieldBuf.array();
    }

    /**
     * 父类转为
     * @param obj
     * @param superClass
     * @return
     */
    public byte[] superClassToByte(Object obj,Class superClass){
        System.out.println("--------------superClassToByte-----------------");
        String className=superClass.getSimpleName();
        byte[]  superByte=new byte[0];
        System.out.println("className: "+className);
        System.out.println("superClass by superClass "+superClass.getSuperclass());
        if (!className.equals("Object")){
            System.out.println(className);
            //递归编码父类
            byte[] byteData=this.superClassToByte(obj,superClass.getSuperclass());

             Field[] fields= superClass.getDeclaredFields();
            superByte=this.fieldToByte(obj,fields);
            ByteBuffer byteBuffer=ByteBuffer.allocate(superByte.length+byteData.length);
            byteBuffer.put(superByte);
            byteBuffer.put(byteData);

            superByte=byteBuffer.array();
        }

        return superByte;
    }

    /**
     * Field转为Byte
     * @param obj
     * @param fields
     * @return
     */
    public byte[] fieldToByte(Object obj,Field[] fields) {

        FieldUtil fieldUtil =new FieldUtil();
       Field[]  fieldArray= fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        byte[] primitiveByte = this.primitiveTypeToByte(primitiveFields, obj);
        byte[] objectByte = this.objectFieldTypeToByte(objectFields, obj);

        System.out.println("fieldToByte---------");
        System.out.println(StringUtil.bytesToString(primitiveByte));
        System.out.println(StringUtil.bytesToString(objectByte));
        System.out.println("fieldToByte---------");
        ByteBuffer buffer=ByteBuffer.allocate(primitiveByte.length+objectByte.length);
        buffer.put(primitiveByte);
        buffer.put(objectByte);
        return buffer.array();
    }

    /**
     * 编码对象类型
     *
     * @param fields
     * @param obj
     * @return
     */
    public byte[] objectFieldTypeToByte(Field[] fields, Object obj) {

        ByteBuffer byteBuf = ByteBuffer.allocate(fields.length);
        byte[] fieldByte;
        for (Field field : fields) {
            String typeName = field.getType().getSimpleName();
            field.setAccessible(true);
//            System.out.println(typeName);
            try {
                if (typeName.equals("String")) {
                    String value = (String) field.get(obj);
                    byte[] valueBytes = new byte[value.getBytes().length + 2];
                    valueBytes[0] = 0x0f;
                    valueBytes[1] = (byte) value.getBytes().length;
                    System.arraycopy(value.getBytes(), 0, valueBytes, 2, value.getBytes().length);
                    byteBuf = this.put(byteBuf, valueBytes);

                } else if (typeName.equals("Integer")) {
                    int value = (int) field.get(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Long")) {
                    long value = (long) field.get(obj);
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Character")) {
                    Character value = (Character) field.get(obj);
                    fieldByte = TypeToByteArray.charToByteArr(value);
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Short")) {

                    Short value = (Short) field.get(obj);
                    fieldByte = TypeToByteArray.shortToByteArr(value);
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Byte")) {
                    Byte value = (Byte) field.get(obj);
                    fieldByte = new byte[1];
                    fieldByte[0] = value;
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Float")) {
                    Float value = (Float) field.get(obj);
                    fieldByte = TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Double")) {
                    Double value = (Double) field.get(obj);
                    fieldByte = TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if (typeName.equals("Boolean")) {
                    Boolean value = (Boolean) field.get(obj);
                    if (value) {
                        fieldByte = TypeToByteArray.intToByteArr(1);
                    } else {
                        fieldByte = TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf = this.put(byteBuf, fieldByte);
                } else if ("Object".equals(typeName)) {
                        Object value = field.get(obj);
                    if(value!=null) {
                        fieldByte = value.toString().getBytes();
                        byte[] valueBytes = new byte[fieldByte.length + 2];
                        valueBytes[0] = 0x0f;
                        valueBytes[1] = (byte) fieldByte.length;
                        System.arraycopy(fieldByte, 0, valueBytes, 2, fieldByte.length);

                        byteBuf = this.put(byteBuf, valueBytes);
                    }
                } else {
                    //ClassType
                    String className = field.getType().getTypeName();
//                    System.out.println(field.get(Class.forName(className).newInstance()));

//                    System.out.println(field.get(obj));
                    byte[] classObject= this.classTypeToByte(field.get(obj));
                    byteBuf=this.put(byteBuf,classObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        byte[] resultByte = new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(), 0, resultByte, 0, byteBuf.position());


        return resultByte;
    }

    public byte[] classTypeToByte(Object obj) {
        FieldUtil fieldUtil = new FieldUtil();
        System.out.println("------------------------classTypeToByte-----------------------------");
        byte[] resultByte = new byte[0];
        try {
            //所有声明的字段字段
            Field[] fieldArray = obj.getClass().getDeclaredFields();

            for (Field field: fieldArray){
                System.out.println(field);
            }

            //对字段按名称排序
            fieldArray = fieldUtil.fieldSort(fieldArray);

            Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
            Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

            byte[] primitiveByte = this.primitiveTypeToByte(primitiveFields, obj);
            byte[] objectByte = this.objectFieldTypeToByte(objectFields, obj);

            System.out.println("对象类型：" + StringUtil.bytesToString(objectByte));
            System.out.println("值类型：" + StringUtil.bytesToString(primitiveByte));

            resultByte=new byte[primitiveByte.length+objectByte.length+2];
            resultByte[0]=0x1f;
            resultByte[1]= (byte) (primitiveByte.length+objectByte.length);  //todo   现在长度只是一个字节，后面要修改
            System.arraycopy(primitiveByte,0,resultByte,2,primitiveByte.length);
            System.arraycopy(objectByte,0,resultByte,primitiveByte.length+2,objectByte.length);

            //todo 字段为类类型的父类还没处理
//            byte[] superClassByte= this.superClassToByte(obj,obj.getClass().getSuperclass());
//            ByteBuffer byteBuffer=ByteBuffer.allocate(resultByte.length+superClassByte.length);
//            byteBuffer.put(resultByte);
//            byteBuffer.put(superClassByte);
//
//            resultByte=byteBuffer.array();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultByte;
    }

    /**
     * 原始类型转换为byte数组
     *
     * @param fields
     * @param obj
     * @return
     */
    public byte[] primitiveTypeToByte(Field[] fields, Object obj) {
        ByteBuffer byteBuf = ByteBuffer.allocate(fields.length * 8);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String typeName = field.getType().getName();
            field.setAccessible(true);
            byte[] fieldByte;
            try {

                if (typeName.equals("int")) {
                    int value = field.getInt(obj);
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("long")) {
                    long value = field.getLong(obj);
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("boolean")) {
                    boolean value = field.getBoolean(obj);
                    if (value) {
                        fieldByte = TypeToByteArray.intToByteArr(1);
                    } else {
                        fieldByte = TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("char")) {
                    char value = field.getChar(obj);
                    fieldByte = TypeToByteArray.charToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("float")) {
                    float value = field.getFloat(obj);
                    fieldByte = TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("double")) {
                    double value = field.getDouble(obj);
                    fieldByte = TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("short")) {
                    short value = field.getShort(obj);
                    fieldByte = TypeToByteArray.shortToByteArr(value);
                    byteBuf.put(fieldByte);
                } else if (typeName.equals("byte")) {
                    byte value = field.getByte(obj);
                    fieldByte = new byte[1];
                    fieldByte[0] = value;
                    byteBuf.put(fieldByte);
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(StringUtil.bytesToString(primitiveByte));
        byte[] resultByte = new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(), 0, resultByte, 0, byteBuf.position());
//        System.out.println(StringUtil.bytesToString(resultByte));
        return resultByte;
    }

    public String getClassName(Object obj) {
        String className = obj.getClass().getName();

        return className;
    }


}
