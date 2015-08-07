package co.solinx.forestserial.test;

import co.solinx.forestserial.ForestSerialized;
import co.solinx.forestserial.test.data.Response;
import co.solinx.forestserial.test.data.Test;
import co.solinx.forestserial.exception.ClassException;
import co.solinx.forestserial.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linx on 2015/8/6.
 */
public class SeralizeToByte {

    public static void main(String[] args){
        Test forestSerial=new Test();
        Response response=new Response();
        response.setSn(199999992);
        response.setBaseID(9999);
        response.setBaseClassID(22222);
        response.setResult(new ClassException("124"));

        forestSerial.setA(221);
        forestSerial.setB(122);


//        forestSerial.setCr('A');
        forestSerial.setCharNum('Z');
        forestSerial.setFl(10.9f);
        forestSerial.setDl(20.3d);
        forestSerial.setBooleanNum(true);
        forestSerial.setBl(true);
        forestSerial.setBa("string");

        Response temp= new Response();
        temp.setResult("Object");
        forestSerial.setObj(temp);


        forestSerial.setSt((short) 20);
        forestSerial.setBt((byte) 12);
        forestSerial.setCc(12345678);
        forestSerial.setInte(612);
        forestSerial.setByteNum((byte) 1);
        forestSerial.setLongNum(987l);
        forestSerial.setShortNum((short) 3);
        forestSerial.setFloatNum(20.6f);
        forestSerial.setSuperSn(888);
        forestSerial.setResponse(response);
        forestSerial.setBaseID(7477);
        forestSerial.setBaseClassID(6666);

        List<Integer> integerList=new ArrayList<>();
        integerList.add(12);
        integerList.add(13);
        integerList.add(8888);

        forestSerial.setIntegerList(integerList);

        List<String> stringList=new ArrayList<>();
        stringList.add("sList");
        stringList.add("tempList");
        forestSerial.setStringList(stringList);


        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("arrayList");
        forestSerial.setArrayList(arrayList);

        ArrayList<Long> longArrayList=new ArrayList<>();
        longArrayList.add(12l);
        forestSerial.setLongList(longArrayList);


        ArrayList<Character> charList=new ArrayList<>();
        charList.add('A');
        charList.add('B');
        forestSerial.setCharList(charList);


        Map<String,Integer> mapString=new HashMap<>();
        mapString.put("String", 123);
        mapString.put(String.valueOf(113), 11);
        forestSerial.setMap(mapString);

        Map<Short,Short> integerMap=new HashMap<>();
        integerMap.put((short)110,(short)120);
        forestSerial.setIntegerMap(integerMap);

//        forestSerial.setBackage(Color.red);

        int[] arry=new int[5];
        arry[0]=1;
        forestSerial.setArry(arry);

        ForestSerialized serialized=new ForestSerialized();
        byte[] datas= serialized.serializeToByte(forestSerial);

        System.out.println(StringUtil.bytesToString(datas));
        System.out.println(serialized.deSerializeToObject(datas));
    }


}
