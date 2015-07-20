package co.solinx.forestserial.serializer;

import co.solinx.forestserial.common.ByteBufferTool;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by linx on 2015/7/2.
 */
public class FieldInfo {

    Field[] fields;
    ClassInfo classInfo;
    FieldUtil fieldUtil = new FieldUtil();

    public FieldInfo(ClassInfo classInfo){
        fields=classInfo.getDeclaredFields();
        this.classInfo=classInfo;
     }


    /**
     * Field转为Byte
     *
     * @param obj 对象
     * @param fields 字段
     * @return 字段转换为bute都的数组
     */
    public byte[] fieldToByte(Object obj, Field[] fields) {


        Field[] fieldArray = fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        byte[] primitiveByte = this.primitiveTypeToByte(primitiveFields, obj);
        byte[] objectByte = this.objectFieldTypeToByte(objectFields, obj);

        System.out.println("原始类型： " + StringUtil.bytesToString(primitiveByte));
        System.out.println("对象类型： " + StringUtil.bytesToString(objectByte));
        ByteBuffer buffer = ByteBuffer.allocate(primitiveByte.length + objectByte.length);
        buffer.put(primitiveByte);
        buffer.put(objectByte);
        return buffer.array();
    }


    public byte[] fieldToByte(Object obj){
        System.out.println("------------------------classTypeToByte-----------------------------");
        byte[] resultByte = new byte[0];
        try {
            if (obj == null) {
                resultByte = new byte[2];
                resultByte[0] = 0x1f;
                resultByte[1] = 0;
                return resultByte;
            }
            //所有声明的字段字段
            Field[] fieldArray = obj.getClass().getDeclaredFields();

            //对字段按名称排序
            fieldArray = fieldUtil.fieldSort(fieldArray);
              byte[] fieldByte=  this.fieldToByte(obj,fieldArray);

            resultByte = new byte[fieldByte.length + 2];
            resultByte[0] = 0x1f;
            resultByte[1] = (byte) (fieldByte.length);  //todo   现在长度只是一个字节，后面要修改
            System.arraycopy(fieldByte, 0, resultByte, 2, fieldByte.length);

            //todo 字段为类类型的父类还没处理
            System.out.println("-------------------------字段的父类 " + obj.getClass().getSuperclass());
            byte[] superClassByte = classInfo.superClassToByte(obj, obj.getClass().getSuperclass());
            System.out.println("-------------------------字段的父类 " + StringUtil.bytesToString(superClassByte));

            ByteBuffer byteBuffer = ByteBuffer.allocate(resultByte.length + superClassByte.length);
            byteBuffer.put(resultByte);
            byteBuffer.put(superClassByte);
//
            resultByte = byteBuffer.array();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultByte;
    }



    /**
     * 原始类型转换为byte数组
     *
     * @param fields 字段组
     * @param obj 当前对象
     * @return 编码的byte
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
        byte[] resultByte = new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(), 0, resultByte, 0, byteBuf.position());
        return resultByte;
    }

    /**
     * 编码对象类型
     *
     * @param fields 字段组
     * @param obj 对象
     * @return 编码后的byte
     */
    public byte[] objectFieldTypeToByte(Field[] fields, Object obj) {

        ByteBuffer byteBuf = ByteBuffer.allocate(fields.length);
        byte[] fieldByte;
        for (Field field : fields) {
            String typeName = field.getType().getSimpleName();
            field.setAccessible(true);
            System.out.println(typeName);
            try {
                if (typeName.equals("String")) {
                    String value = (String) field.get(obj);
                    int byteLength = 2;
                    int valueLength;
                    if (value != null) {
                        byteLength = byteLength + value.getBytes().length;
                        valueLength = value.getBytes().length;
                    } else {
                        valueLength = 0;
                    }
                    byte[] valueBytes = new byte[byteLength];
                    valueBytes[0] = 0x0f;
                    valueBytes[1] = (byte) valueLength;   //todo 待处理，现在只是一个字节长度
                    if (value != null) {
                        System.arraycopy(value.getBytes(), 0, valueBytes, 2, value.getBytes().length);
                    }
                    byteBuf = ByteBufferTool.put(byteBuf, valueBytes);
                } else if (typeName.equals("Integer")) {
                    Integer value = (Integer) field.get(obj);
                    if (value == null) {
                        value = 0;
                    }
                    fieldByte = TypeToByteArray.intToByteArr(value);
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Long")) {
                    Long value = (Long) field.get(obj);
                    if (value == null) {
                        value = 0l;
                    }
                    fieldByte = TypeToByteArray.longToByteArr(value);
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);
                } else if (typeName.equals("Character")) {
                    Character value = (Character) field.get(obj);
                    if (value == null) {
                        value = 0;
                    }
                    fieldByte = TypeToByteArray.charToByteArr(value);
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Short")) {
                    Short value = (Short) field.get(obj);
                    if (value == null) {
                        value = 0;
                    }
                    fieldByte = TypeToByteArray.shortToByteArr(value);
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Byte")) {
                    Byte value = (Byte) field.get(obj);

                    fieldByte = new byte[1];
                    if (value != null) {
                        fieldByte[0] = value;
                    } else {
                        fieldByte[0] = 0;
                    }
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Float")) {
                    Float value = (Float) field.get(obj);
                    if (value == null) {
                        value = 0f;
                    }
                    fieldByte = TypeToByteArray.intToByteArr(Float.floatToIntBits(value));
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Double")) {
                    Double value = (Double) field.get(obj);
                    if (value == null) {
                        value = 0d;
                    }
                    fieldByte = TypeToByteArray.longToByteArr(Double.doubleToLongBits(value));
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if (typeName.equals("Boolean")) {
                    Boolean value = (Boolean) field.get(obj);
                    if (value == null) {
                        value = false;
                    }
                    if (value) {
                        fieldByte = TypeToByteArray.intToByteArr(1);
                    } else {
                        fieldByte = TypeToByteArray.intToByteArr(0);
                    }
                    byteBuf = ByteBufferTool.put(byteBuf, fieldByte);

                } else if ("Object".equals(typeName)) {
                    Object value = field.get(obj);
                    byte[] valueBytes;
                    if (value != null) {
                        fieldByte = value.toString().getBytes();
                        valueBytes = new byte[fieldByte.length + 2];
                        valueBytes[0] = 0x0f;
                        valueBytes[1] = (byte) fieldByte.length;
                        System.arraycopy(fieldByte, 0, valueBytes, 2, fieldByte.length);
                    } else {
                        valueBytes = new byte[2];
                        valueBytes[0] = 0x0f;
                        valueBytes[1] = 0;
                    }
                    byteBuf = ByteBufferTool.put(byteBuf, valueBytes);
                }else if("List".equals(typeName)||"ArrayList".equals(typeName)){
                    if(field.get(obj)!=null) {
                        Type type = field.getGenericType();
                        fieldByte = new byte[0];
                        if (type instanceof ParameterizedType) {
                            Type clazz = ((ParameterizedType) type).getActualTypeArguments()[0];

                            if ("java.lang.Integer".equals(clazz.getTypeName())) {
                                System.out.println("java.lang.Integer------------------   "+clazz.getTypeName());
                                List<Integer> value = (List<Integer>) field.get(obj);
                                ByteBuffer buffer=ByteBuffer.allocate(value.size()*4);
                                for (Integer temp : value) {
                                    fieldByte = TypeToByteArray.intToByteArr(temp);
                                    buffer.put(fieldByte);
                                    System.out.println(StringUtil.bytesToString(fieldByte));
                                }
                                byte[] byteData=new byte[buffer.position()+2];
                                byteData[0]= (byte) 0xf2;
                                byteData[1]= (byte) value.size();
                                System.arraycopy(buffer.array(),0,byteData,2,buffer.position());
                                fieldByte=byteData;
                            }else if("java.lang.String".equals(clazz.getTypeName())){
                                System.out.println("clazz------------------   "+clazz.getTypeName());
                               List<String> value=(List<String>)field.get(obj);
                                ByteBuffer buffer=ByteBuffer.allocate(4);
                                for (String temp : value){
                                    buffer.put((byte) temp.getBytes().length);
                                    buffer=ByteBufferTool.put(buffer,temp.getBytes());
                                }
                               byte[] byteData=new byte[buffer.position()+2];
                                byteData[0]=(byte)0xf3;
                                byteData[1]= (byte) value.size();
                                System.arraycopy(buffer.array(),0,byteData,2,buffer.position());
                                fieldByte=byteData;
                            }

                        }
                    }else{
                        fieldByte=new byte[1];
                        fieldByte[0]=0;
                    }
                    byteBuf=ByteBufferTool.put(byteBuf, fieldByte);
                } else {
                    //编码类
                    String className = field.getType().getTypeName();
                    Object value = field.get(obj);
                    if(value!=null) {
                        byte[] classObject = this.fieldToByte(value);
                        byteBuf = ByteBufferTool.put(byteBuf, classObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        byte[] resultByte = new byte[byteBuf.position()];
        System.arraycopy(byteBuf.array(), 0, resultByte, 0, byteBuf.position());


        return resultByte;
    }

}
