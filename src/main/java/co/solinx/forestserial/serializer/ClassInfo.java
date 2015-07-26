package co.solinx.forestserial.serializer;

import co.solinx.forestserial.Buffer.BufferStream;
import co.solinx.forestserial.util.FieldUtil;

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

    public Object getInstanceObject(){
        this.obj = this.getClazzInstance(this.getClassName());
        return obj;
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
    public BufferStream deField(){

       Field[] fields=this.getDeclaredFields();
        Field[]  fieldArray= fieldUtil.fieldSort(fields);


        DeFieldInfo deFieldInfo=new DeFieldInfo();

        deFieldInfo.deField(obj,fieldArray,bufferStream);
        return bufferStream;
    }


    public void deField(Object obj,Field[] fields,BufferStream bufferStream){
        this.obj=obj;

        Field[]  fieldArray= fieldUtil.fieldSort(fields);

        Field[] primitiveFields = fieldUtil.getPrimitiveTypeField(fieldArray);
        Field[] objectFields = fieldUtil.getObjectTypeField(fieldArray);

        DeFieldInfo deFieldInfo=new DeFieldInfo();


        deFieldInfo.dePrimitiveField(obj, primitiveFields, bufferStream);
        deFieldInfo.deObjectField(obj, objectFields, bufferStream);


    }

    public void superClassDeCode(Object obj,Class superClass){
        String className=superClass.getSimpleName();
        System.out.println("className : " + className);
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



    public Field[]  getDeclaredFields(){
        if (obj==null){
            this.obj = this.getClazzInstance(this.getClassName());
        }
        return obj.getClass().getDeclaredFields();
    }

    public byte[] fieldBytes(){
        //所有声明的字段字段
        return fieldInfo.fieldToByte(obj, fieldInfo.fields);
    }


    /**
     * encode父类
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
            //字段
            Field[] fields = superClass.getDeclaredFields();
            superByte = fieldInfo.fieldToByte(obj, fields);
            ByteBuffer byteBuffer = ByteBuffer.allocate(superByte.length + byteData.length);
            byteBuffer.put(superByte);
            byteBuffer.put(byteData);
            superByte = byteBuffer.array();
        }

        return superByte;
    }


    /**
     * className byte
     * @return
     */
    public byte[] getClassNameByte(){
        byte[] nameByte=classInfo.getName().getBytes();
        byte[] className=new byte[nameByte.length+1];
        className[0]= (byte) nameByte.length;   //类长度
        //类全限定名
        System.arraycopy(nameByte, 0, className,1, nameByte.length);

        return className;
    }

}
