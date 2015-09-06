package co.solinx.forestserial.test.fastjson;

import co.solinx.forestserial.test.data.Test;
import com.alibaba.fastjson.JSON;

/**
 * Created by linx on 2015/9/6.
 */
public class TestJson {

    public static void main(String[] args){

        Test test=new Test();
//        test.setB(2);
        System.out.println(JSON.toJSONString(test));


    }

}
