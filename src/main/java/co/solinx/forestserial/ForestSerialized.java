package co.solinx.forestserial;

import co.solinx.forestserial.coders.ByteDecoder;
import co.solinx.forestserial.coders.ByteEncoder;
import co.solinx.forestserial.serializer.ObjectInput;
import co.solinx.forestserial.serializer.ObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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

    public byte[] serializeToByte(Object obj) {
        return output(obj);
    }

    public Object deSerializeToObject(byte[] data) {
        return input(data);
    }

    private byte[] output(Object obj) {
        byte[] data = new byte[0];
        try {
            ObjectOutput output = new ObjectOutput();
            output.writeObject(obj);

            data = output.toBytes();
            data = compress(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private Object input(byte[] data) {
        Object result = null;
        byte[] proData = uncompress(data);
        ObjectInput input = new ObjectInput(proData);
        try {
            result=input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private byte[] uncompress(byte[] data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] result = new byte[20];
        try {
            GZIPInputStream gzipInput = new GZIPInputStream(inputStream);
            while (gzipInput.read(result) != -1) {
                outputStream.write(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private byte[] compress(byte[] data) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream goutput = new GZIPOutputStream(arrayOutputStream);
            goutput.write(data);
            goutput.finish();
            goutput.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOutputStream.toByteArray();
    }


    public static void main(String[] args) {

    }

}
