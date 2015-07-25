package co.solinx.forestserial.test.fst;

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
    }

}
