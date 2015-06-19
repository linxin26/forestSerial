package co.solinx.forestserial.test;

import java.io.Serializable;

/**
 * Created by linx on 2015/6/19.
 */
public class Test extends Response implements Serializable {

    //    private float fl = 10;
    private int baaaa = 10;
    private int d= 11;
    private String message = "a1";
    private String msg = "b";
    private Integer integ = 120;
    private Object test = "12";
    private long size = 120;
    private boolean bool = true;
    private int zm = 90;

//    public void setR(Request request) {
//        r = request;
//    }


    @Override
    public String toString() {
        return "Test{" +
//                "r=" + r +
//                ", id=" + id +
                ", sn=" + sn +
                ", message='" + message + '\'' +
                ", msg='" + msg + '\'' +
                ", integ=" + integ +
                ", test=" + test +
                ", size=" + size +
                ", bool=" + bool +
                ", zm=" + zm +
                '}';
    }

}
