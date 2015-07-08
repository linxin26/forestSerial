package co.solinx.forestserial.serializer;

import co.solinx.forestserial.Buffer.BufferStream;
import co.solinx.forestserial.common.CodeType;
import co.solinx.forestserial.util.FieldUtil;
import co.solinx.forestserial.util.StringUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by linx on 2015/7/1.
 */
public class ClassInfo {


    Class classInfo;
    FieldInfo fieldInfo;
    Object obj;
    BufferStream bufferStream;
    FieldUtil fieldUtil = new FieldUtil();
    String className;

    public ClassInfo(Object obj) {
        try {
            classInfo= Class.forName(obj.getClass().getName());
            this.obj=obj;
            fieldInfo=new FieldInfo(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    public ClassInfo(byte[] dataByte){
        bufferStream=new BufferStream(dataByte);
    }

    /**
     * 类名
     * @return
     */
    public String getClassName(){
        if (bufferStream!=null){
             int flat=bufferStream.getByte();
            int length=bufferStream.getByte();
            className= bufferStream.getString(length);
        }else{
            className=classInfo.getSimpleName();
        }
        return className;
    }

    public Object getClazzInstance(String clazzName) {

        Object instance = null;
        try {
            System.out.println("--------------------------------- "+clazzName);
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

    /**
     * 解码字段
     */
    public BufferStream deField(Object obj){
        this.obj=obj;
       Field[] fields=this.getDeclaredFields();
        Field[]  fieldArray= fieldUtil.fieldSort(fields);


        DeFieldInfo deFieldInfo=new DeFieldInfo();

        deFieldInfo.deField(obj,fieldArray,bufferStream);
        return bufferStream;
    }


    public void superClassDeCode(Object obj,Class superClass){
        String className=superClass.getSimpleName();
        System.out.println("className : "+className);
        if (!"Object".equals(className)){
            Field[] fields= superClass.getDeclaredFields();

//            for (Field field: fields){
//                System.out.println(field);
//            }

            this.deField(obj, fields, bufferStream);
            System.out.println("getPosition  :  " + bufferStream.getPosition());
//            System.out.println("getPosition  :  " + bufferStream.getPosition()+"  —— "+bufferStream.getByte());
//            System.out.println(" currentClass ： " + superClass + "  superClass.getSuperclass： " + superClass.getSuperclass());
            this.superClassDeCode(obj,superClass.getSuperclass());
        }
    }

    public void deField(Object obj,Field[] fields,BufferStream bufferStream){
        this.obj=obj;

        Field[]  fieldArray= fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        DeFieldInfo deFieldInfo=new DeFieldInfo();


        deFieldInfo.dePrimitiveField(obj, primitiveFields, bufferStream);
        deFieldInfo.deObjectField(obj,objectFields,bufferStream);


    }



    public Field[]  getDeclaredFields(){
        return obj.getClass().getDeclaredFields();
    }

    public byte[] fieldBytes(){
        //所有声明的字段字段
        return fieldInfo.fieldToByte(obj, fieldInfo.fields);
    }


    public byte[] classTypeToByte(Object obj) {

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

            Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
            Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

            byte[] primitiveByte = fieldInfo.primitiveTypeToByte(primitiveFields, obj);
            byte[] objectByte = fieldInfo.objectFieldTypeToByte(objectFields, obj);

            System.out.println("对象类型：" + StringUtil.bytesToString(objectByte));
            System.out.println("值类型：" + StringUtil.bytesToString(primitiveByte));

            resultByte = new byte[primitiveByte.length + objectByte.length + 2];
            resultByte[0] = 0x1f;
            resultByte[1] = (byte) (primitiveByte.length + objectByte.length);  //todo   现在长度只是一个字节，后面要修改
            System.arraycopy(primitiveByte, 0, resultByte, 2, primitiveByte.length);
            System.arraycopy(objectByte, 0, resultByte, primitiveByte.length + 2, objectByte.length);

            //todo 字段为类类型的父类还没处理
            System.out.println("-------------------------字段的父类 " + obj.getClass().getSuperclass());
            byte[] superClassByte = this.superClassToByte(obj, obj.getClass().getSuperclass());
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
     * 父类转为
     *
     * @param obj 当前对象
     * @param superClass 父类
     * @return 父类转换为byte数组
     */
    public byte[] superClassToByte(Object obj, Class superClass) {
        System.out.println("--------------superClassToByte-----------------");
        String className = superClass.getSimpleName();
        byte[] superByte = new byte[0];
        System.out.println(" currentClass： " + className + "   superClass： " + superClass.getSuperclass());
        if (!className.equals("Object")) {
            //递归编码父类
            byte[] byteData = this.superClassToByte(obj, superClass.getSuperclass());

            Field[] fields = superClass.getDeclaredFields();
            superByte = fieldInfo.fieldToByte(obj, fields);
            ByteBuffer byteBuffer = ByteBuffer.allocate(superByte.length + byteData.length);
            byteBuffer.put(superByte);
            byteBuffer.put(byteData);

            superByte = byteBuffer.array();
        }

        return superByte;
    }

    public byte[] getClassNameByte(){
        byte[] nameByte=classInfo.getName().getBytes();
        byte[] className=new byte[nameByte.length+2];
        className[0]= CodeType.CLASS_NAME;
        className[1]= (byte) nameByte.length;   //类长度
        //类全限定名
        System.arraycopy(nameByte, 0, className, 2, nameByte.length);

        return className;
    }

    public Object deCodeClassName(){
//        bufferStream

        return null;
    }


}
