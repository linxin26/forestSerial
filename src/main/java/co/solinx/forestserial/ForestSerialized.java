package co.solinx.forestserial;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.data.Response;
import co.solinx.forestserial.data.Test;
import co.solinx.forestserial.serializer.ObjectInput;
import co.solinx.forestserial.serializer.ObjectOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linx on 2015/7/20.
 */
public class ForestSerialized {

    ByteEncoder encoder = new ByteEncoder();
    ByteDecoder decoder = new ByteDecoder();

    public byte[] serialize(Object obj) {


        return encoder.encoder(obj);
    }


    public Object deSerialize(byte[] data) throws Exception {

        return decoder.decoder(data);
    }

    public byte[] enOutput(Object obj){
        ObjectOutput output=new ObjectOutput(null);
        output.writeObject(obj);
        return output.toBytes();
    }

    public Object deInput(byte[] data){
        ObjectInput input=new ObjectInput(data);
        return input.readObject();
    }


    public static void main(String[] args){
        Test forestSerial=new Test();
        Response response=new Response();

        forestSerial.setA(221);
        forestSerial.setB(122);
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
        ForestSerialized serialized=new ForestSerialized();
        byte[] datas= serialized.enOutput(forestSerial);

        System.out.println(serialized.deInput(datas));
    }

}
