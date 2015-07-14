package co.solinx.forestserial.test;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.data.Response;
import co.solinx.forestserial.data.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/6/30.
 */
public class ForestSerialTest {

    public static void main(String[] args){


         Test forestSerial=new Test();
        Response response=new Response();

        forestSerial.setB(123);
        forestSerial.setResponse(response);

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


        ByteDecoder decoder=new ByteDecoder();
        ByteEncoder encoder=new ByteEncoder();
        byte[] byteData= encoder.encoder(forestSerial);

        try {
            System.out.println(decoder.decoder(byteData));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
