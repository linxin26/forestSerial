package co.solinx.forestserial.test;

import co.solinx.forestserial.ForestSerialized;
import co.solinx.forestserial.test.data.Test;
import com.alibaba.fastjson.JSON;
import org.nustaq.serialization.FSTConfiguration;

/**
 * Created by linx on 2015/8/27.
 */
public class SeralizeToJSON {

    public static void main(String[] args){

        Test test=new Test();
        test.setA(1234);
        test.setStringArray(new String[]{"1","2"});
//        test.setBa("string");
        test.setArry(new int[]{2,3});
        test.setInte(54321);
        ForestSerialized forestSerialize=new ForestSerialized();

        System.out.println(forestSerialize.toJsonString(test));

        System.out.println(JSON.toJSONString(test));


//        FSTConfiguration jsonconf=FSTConfiguration.createJsonConfiguration();
//        System.out.println(jsonconf.asJsonString(test));
    }

}
