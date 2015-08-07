package co.solinx.forestserial.test;


import co.solinx.forestserial.util.DataTypeConstant;
import co.solinx.forestserial.util.StringUtil;
import co.solinx.forestserial.util.TypeToByteArray;

import java.lang.reflect.Field;

/**
 * Created by linx on 2015/6/17.
 * forestSerialize序列化
 * 不支持final字段
 */
public class ForestSerialize {

    public static void main(String[] args) {


        Request request = new Request();
        request.setId(10);
        request.setVersion("version2");
        request.setData("data3");
        request.setResponse(null);
        request.setLongv(123456);
        request.setLongL(654321L);
        request.setBool(true);
//        request.setSn(123);
        ForestSerialize serialize = new ForestSerialize();
        serialize.bytesToLong(TypeToByteArray.longToByteArr(256 * 256 * 256));
        System.out.println(request);
        byte[] dataByte = serialize.serialize(request);
        System.out.println(StringUtil.bytesToString(dataByte));
        Request temp = (Request) serialize.deSerialize(dataByte);
        System.out.println(temp);
    }

    /**
     * type+length+value
     *
     * @param obj
     * @return
     */
    public byte[] serialize(Object obj) {
        byte[] objByte = new byte[250];
        int currentLength = 5;    //一个字节类标识+四个字节类名长度
        //从Object中取得对象真实的类
        Class classz = obj.getClass();
        System.out.println(classz.getSuperclass());
        //类全限定名
        String className = classz.getCanonicalName();
        byte[] classNameByte = className.getBytes();
        //0 标识为类
        objByte[0] = DataTypeConstant.CLASS_NAME;
        //四个字节存储类名长度
        System.arraycopy(TypeToByteArray.intToByteArr(classNameByte.length), 0, objByte, 1, 4);
        //存储类名
        System.arraycopy(className.getBytes(), 0, objByte, currentLength, className.getBytes().length);
        //当前长度为当前长度+类名字节长度
        currentLength += className.getBytes().length;
//        System.out.println("classBytes length : "+currentLength);
        //遍历每个字段
        for (Field field : classz.getDeclaredFields()) {
            field.setAccessible(true);
            try {

                String typeName = field.getType().getSimpleName();
                System.out.println(field.getType());
                System.out.println(typeName);
                byte[] temp;
                //根据对应的数据类型转为Byte数组
                switch (typeName) {
                    case "int":
                        objByte[currentLength] = DataTypeConstant.INT;
                        currentLength++;
                        System.arraycopy(TypeToByteArray.intToByteArr(4), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;

                        temp = TypeToByteArray.intToByteArr(field.getInt(obj));
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "Integer":
                        objByte[currentLength] = DataTypeConstant.INTEGER;
                        currentLength++;
                        System.arraycopy(TypeToByteArray.intToByteArr(4), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
                        Object value = field.get(obj);
                        //todo 值为null时，序列化为变为0
                        if (value == null) {
                            temp = TypeToByteArray.intToByteArr(0);
                        } else {
                            temp = TypeToByteArray.intToByteArr((Integer) value);
                        }
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "Long":
                        objByte[currentLength] = DataTypeConstant.LONG_L;
                        currentLength++;
                        System.arraycopy(TypeToByteArray.intToByteArr(8), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
                        temp = TypeToByteArray.longToByteArr((Long) field.get(obj));
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "long":
                        objByte[currentLength] = DataTypeConstant.LONG;
                        currentLength++;
                        System.arraycopy(TypeToByteArray.intToByteArr(8), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
                        temp = TypeToByteArray.longToByteArr((Long) field.get(obj));
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "Object":
                        temp = field.get(obj).toString().getBytes();
                        objByte[currentLength] = DataTypeConstant.OBJECT;
                        currentLength++;
                        System.arraycopy(TypeToByteArray.intToByteArr(temp.length), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "String":
                        objByte[currentLength] = DataTypeConstant.STRING;
                        currentLength++;
                        temp = field.get(obj).toString().getBytes();
                        System.arraycopy(TypeToByteArray.intToByteArr(temp.length), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
                        System.arraycopy(temp, 0, objByte, currentLength, temp.length);
                        currentLength += temp.length;
                        break;
                    case "boolean":
                        objByte[currentLength++]=DataTypeConstant.BOOLEAN;//类型
                        System.arraycopy(TypeToByteArray.intToByteArr(1), 0, objByte, currentLength, 4);
                        currentLength = currentLength + 4;
//                        objByte[currentLength++]=1;//长度
                        boolean bool=field.getBoolean(obj);
                        if(bool){
                            objByte[currentLength++]=1;
                        }else{
                            objByte[currentLength++]=0;
                        }
                        break;

                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        byte[] result = new byte[currentLength];
        System.arraycopy(objByte, 0, result, 0, currentLength);
//        System.out.println(StringUtil.bytesToString(objByte));
//        this.deSerialize(objByte);
        return result;
    }

    public int count(byte[] data) {
        return bytesToInt(data);
    }

    public long bytesToLong(byte[] data){
             long value=0;
        for (int i = 0; i < data.length; i++) {
//            System.out.println(data[i]&0xff);
            if(i==0){
                value+=(data[i]&0xff);
            }else{
                value=(data[i]&0xff)+value*256;
            }

        }
//        System.out.println(value);
        return value;
    }

    public int bytesToInt(byte[] data) {

        int value=0;
        for (int i = 0; i < data.length; i++) {
            if(i==0){
                value+=(data[i]&0xff);
            }else{
                value=(data[i]&0xff)+value*256;
            }

        }
//        System.out.println(value);
//        for (int temp : data) {
//            System.out.println(temp);
//        }
        return value;
    }


    public Object deSerialize(byte[] data) {
        byte[] classLength = new byte[4];
        System.arraycopy(data, 1, classLength, 0, 4);
//        System.out.println(this.count(classLength));
        byte[] classNameByte = new byte[this.bytesToInt(classLength)];
        System.arraycopy(data, 5, classNameByte, 0, this.bytesToInt(classLength));
        String className = new String(classNameByte);
//        System.out.println(className);

        int index = 5 + classNameByte.length;

        Object cObject = null;
        try {
            Class obj = Class.forName(className);
            cObject = obj.newInstance();
            while (index < data.length) {

                    if (index >= data.length) {
                        break;
                    }
                    byte type = data[index++];

//                    field.setAccessible(true);
//                    String typeName = field.getType().getSimpleName();
////                    System.out.println("typeName:"+typeName);
//                    System.out.println("type:"+type+" typeName:"+typeName);

                    byte[] length = new byte[4];
                    if (type == DataTypeConstant.INT ) {
//                        byte[] serializeByte = new byte[4];
                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;
                        int bodyLength = TypeToByteArray.hBytesToInt(length);
                        byte[] dataO = new byte[bodyLength];
//                        System.out.println(StringUtil.bytesToString(length));
                        System.arraycopy(data, index, dataO, 0, bodyLength);

                        Field[] fields = cObject.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String typeName = field.getType().getSimpleName();
                            if (typeName.equals(DataTypeConstant.INT_N)) {
                                System.out.println(this.bytesToInt(dataO));
                                field.set(cObject,this.bytesToInt(dataO));
                            }
                        }
                        index = index + dataO.length;
                    }else if (type == DataTypeConstant.INTEGER ) {
//                        byte[] serializeByte = new byte[4];
                    System.arraycopy(data, index, length, 0, 4);
                    index = index + 4;
                    int bodyLength = TypeToByteArray.hBytesToInt(length);
                    byte[] dataO = new byte[bodyLength];
//                        System.out.println(StringUtil.bytesToString(length));
                    System.arraycopy(data, index, dataO, 0, bodyLength);

                    Field[] fields = cObject.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String typeName = field.getType().getSimpleName();
                        if (typeName.equals(DataTypeConstant.INTEGER_N)) {
//                            System.out.println(this.bytesToInt(dataO));
                            field.set(cObject,this.bytesToInt(dataO));
                        }
                    }
                    index = index + dataO.length;
                } else if (type == DataTypeConstant.OBJECT ) {
//                        byte[] length = new byte[4];
                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;
                        int bodyLength = TypeToByteArray.hBytesToInt(length);
                        byte[] dataO = new byte[bodyLength];
                        System.arraycopy(data, index, dataO, 0, bodyLength);
                        Field[] fields = cObject.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String typeName = field.getType().getSimpleName();
                            if (typeName.equals(DataTypeConstant.OBJECT_N)) {
                                field.set(cObject, new String(dataO));
                            }
                        }
                        index = index + dataO.length;
                    } else if (type == DataTypeConstant.STRING ) {
//                        byte[] length = new byte[4];
                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;
                        int bodyLength = TypeToByteArray.hBytesToInt(length);

                        byte[] dataO = new byte[bodyLength];
                        System.arraycopy(data, index, dataO, 0, bodyLength);

                        Field[] fields = cObject.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String typeName = field.getType().getSimpleName();
                            if (typeName.equals(DataTypeConstant.STRING_N) ){
                                field.set(cObject, new String(dataO));
                            }
                        }
                        index = index + dataO.length;
                    } else if (type == DataTypeConstant.LONG ) {
//                        byte[] length = new byte[4];

                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;
                        int bodyLength = TypeToByteArray.hBytesToInt(length);
                        byte[] dataO = new byte[bodyLength];
                        System.arraycopy(data, index, dataO, 0, bodyLength);
//                        System.out.println(StringUtil.bytesToString(dataO));
                    Field[] fields = cObject.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String typeName = field.getType().getSimpleName();
                        if ( typeName.equals(DataTypeConstant.LONG_N)|| typeName.equals(DataTypeConstant.LONG_LN)) {
                            field.set(cObject, (long) this.count(dataO));
                        }
                    }
                        index = index + dataO.length;

                    }else if (type == DataTypeConstant.LONG_L ) {
//                        byte[] length = new byte[4];

                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;
                        int bodyLength = TypeToByteArray.hBytesToInt(length);
                        byte[] dataO = new byte[bodyLength];
                        System.arraycopy(data, index, dataO, 0, bodyLength);
//                        System.out.println(StringUtil.bytesToString(dataO));
                        Field[] fields = cObject.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            String typeName = field.getType().getSimpleName();
                            if (typeName.equals(DataTypeConstant.LONG_LN)) {
                                field.set(cObject, (long) this.count(dataO));
                            }
                        }
                        index = index + dataO.length;

                    }else if(type==DataTypeConstant.BOOLEAN){
                        System.arraycopy(data, index, length, 0, 4);
                        index = index + 4;   //数据长度
                        int bodyLength = TypeToByteArray.hBytesToInt(length);
                        byte[] dataO = new byte[bodyLength];
                        System.arraycopy(data, index, dataO, 0, bodyLength);

                    Field[] fields = cObject.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String typeName = field.getType().getSimpleName();
                        if (typeName.equals(DataTypeConstant.BOOLEAN_N)) {
                            boolean bool=false;
                            if(this.bytesToInt(dataO)==1){
                                bool=true;
                            }
                            field.set(cObject,  bool);
                        }
                    }
                        index = index + dataO.length;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cObject;
    }

}
