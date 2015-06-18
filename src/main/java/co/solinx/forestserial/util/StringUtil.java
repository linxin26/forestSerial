package co.solinx.forestserial.util;

/**
 * Created by linx on 2015/6/17.
 */
public class StringUtil {

    public static String bytesToString(byte[] data) {

        if(data == null || data.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            sb.append(" " + String.format("%1$02x", data[i]));
        }

        return sb.toString();
    }
}
