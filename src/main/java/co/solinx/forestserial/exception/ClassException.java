package co.solinx.forestserial.exception;

/**
 * Created by linx on 2015/7/2.
 */
public class ClassException extends RuntimeException{

    private int errorCode;

    public ClassException(String message) {
        super(message);
    }

    public ClassException() {
        super();
    }

    public ClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
