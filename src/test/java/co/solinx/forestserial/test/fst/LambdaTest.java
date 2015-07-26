package co.solinx.forestserial.test.fst;

import java.util.Arrays;
import java.util.List;

/**
 * Created by linx on 2015/7/24.
 */
public class LambdaTest {


    public static void main(String[] args) {
        Runnable rn = new Runnable() {
            @Override
            public void run() {
                System.out.println("lambdaTest");
            }
        };
        rn.run();

        m1(Arrays.asList(args));
    }

    public static void m1(List<String> list) {
        list.sort((a, b) -> a.length() - b.length());
    }

}
