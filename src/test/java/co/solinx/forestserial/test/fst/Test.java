package co.solinx.forestserial.test.fst;

import co.solinx.forestserial.util.StringUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/6/19.
 */
public class Test   implements Serializable {

//        private float fl = 10.5f;
//    private int baaaa = 128;
//    private int d= 11;
//    private String message = "a1";
//    private String msg = "b";
//    private Object obj="obj";
//    private Response response=new Response();
//    private Response aa=new Response();
//    private Response bb=new Response();

//    private Integer integ = 120;
//    private Object test = "12";
//    private long size = 120;
//    private boolean bool = true;
    private int zm = 18239000;

//    public void setR(Request request) {
//        r = request;
//    }
    private Integer integer;

//    List<String> integerList=new ArrayList<>();

//
//    public List<String> getIntegerList() {
//        return integerList;
//    }
//
//    public void setIntegerList(List<String> integerList) {
//        this.integerList = integerList;
//    }

    @Override
    public String toString() {
        return "Test{" +
//                "r=" + r +
//                ", id=" + id +
//                ", sn=" + sn +
//                ", message='" + message + '\'' +
//                ", msg='" + msg + '\'' +
//                ", integ=" + integ +
//                ", test=" + test +
//                ", size=" + size +
//                ", bool=" + bool +
                ", zm=" + zm +
                '}';
    }

//    public Response getAa() {
//        return aa;
//    }
//
//    public Response getResponse() {
//        return response;
//    }
//
//    public Response getBb() {
//        return bb;
//    }

    public static void main(String[] args){
        ByteBuffer byteBuf=ByteBuffer.allocate(1);
        byte[] oneByte=new byte[]{1,2,3};
        byte[] threeeByte=new byte[]{4};
        byte[] twoByte=new byte[]{5,6,7,8};
        byte[] bytes=new byte[]{9,10};


        byteBuf=Test.put(byteBuf,oneByte);
        byteBuf=Test.put(byteBuf,threeeByte);
        byteBuf=Test.put(byteBuf,twoByte);

        byteBuf=Test.put(byteBuf,bytes);
System.out.print(StringUtil.bytesToString(byteBuf.array()));
    }

    public static ByteBuffer put(ByteBuffer byteBuf,byte[] bytes){
        ByteBuffer tempBuf;
        System.out.println("剩余："+byteBuf.remaining());
        System.out.println("position："+byteBuf.position());
        System.out.println(StringUtil.bytesToString(bytes));

        if(byteBuf.remaining()<bytes.length){
            tempBuf=ByteBuffer.allocate(byteBuf.array().length + bytes.length);
            tempBuf.clear();
            byte[] curByte=new byte[byteBuf.position()];
            System.arraycopy(byteBuf.array(),0,curByte,0,byteBuf.position());
            tempBuf.put(curByte);
            tempBuf.put(bytes);
            System.out.println("扩容");
//            System.out.println(StringUtil.bytesToString(bytes));
            System.out.println(StringUtil.bytesToString(tempBuf.array()));
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

}
