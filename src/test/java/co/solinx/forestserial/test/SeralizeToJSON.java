package co.solinx.forestserial.test;

import co.solinx.forestserial.ForestSerialized;
import co.solinx.forestserial.test.data.Test;
import com.alibaba.fastjson.JSON;
import org.nustaq.serialization.FSTConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linx on 2015/8/27.
 */
public class SeralizeToJSON {

    public static void main(String[] args){

        Test test=new Test();
        test.setA(1234);
        test.setStringArray(new String[]{"1", "2"});
//        test.setBa("string");
        test.setArry(new int[]{2, 3});
        test.setInte(54321);
        test.setB(222);
        test.setSt((short) 13);
        test.setBl(true);
        test.setBt((byte) 9);
        test.setCr('q');
        test.setFl(12.5f);
        test.setDl(23.6);
        test.setCc(99999);
        test.setStringString("StringStr");
        test.setByteNum((byte) 99);
        test.setFl(99.9f);
        test.setDoubleNum(99.99);
        Map map=new HashMap<>();
        map.put("key","2");
        map.put("key4",4);
        test.setMap(map);
        ForestSerialized forestSerialize=new ForestSerialized();

        System.out.println(forestSerialize.toJsonString(test));

        System.out.println(JSON.toJSONString(test));


//        FSTConfiguration jsonconf=FSTConfiguration.createJsonConfiguration();
//        System.out.println(jsonconf.asJsonString(test));
    }

}
