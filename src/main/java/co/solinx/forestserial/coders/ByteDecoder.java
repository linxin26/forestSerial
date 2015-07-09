package co.solinx.forestserial.coders;

import co.solinx.forestserial.serializer.ClassInfo;

/**
 * Created by linx on 2015-06-22.
 */
public class ByteDecoder implements Decoder {

    public Object decoder(byte[] byteData) throws Exception {


        System.out.println("-------------------------------decoder-------------------------");
        int flag = byteData[0];   //标志位
        int claLength = byteData[1];   //类名长度
        byte[] claNameByte = new byte[claLength];
        System.arraycopy(byteData, 2, claNameByte, 0, claLength);
        String claName = new String(claNameByte);

        byte[] fieldByte = new byte[byteData.length - (claLength + 2)];
        System.arraycopy(byteData, claLength + 2, fieldByte, 0, fieldByte.length);
        System.out.println("解码类名： " + claName);

        ClassInfo classInfo = new ClassInfo(byteData);
        Object clazz = this.getClazzInstance(classInfo.getClassName());
        //解码字段
        classInfo.deField(clazz);

        //解码父类
        classInfo.superClassDeCode(clazz, clazz.getClass().getSuperclass());

        return clazz;
    }


    public Object getClazzInstance(String clazzName) {

        Object instance = null;
        try {
            Class cla = Class.forName(clazzName);
            instance = cla.newInstance();
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
