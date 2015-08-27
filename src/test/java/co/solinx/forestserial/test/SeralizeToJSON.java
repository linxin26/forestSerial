package co.solinx.forestserial.test;

import co.solinx.forestserial.ForestSerialized;
import co.solinx.forestserial.test.data.Test;

/**
 * Created by linx on 2015/8/27.
 */
public class SeralizeToJSON {

    public static void main(String[] args){

        Test test=new Test();

        ForestSerialized forestSerialize=new ForestSerialized();
        System.out.println(forestSerialize.toJsonString(test));
    }

}
