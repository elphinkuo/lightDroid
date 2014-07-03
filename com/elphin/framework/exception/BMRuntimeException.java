package com.elphin.framework.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author elphin
 * @version 1.0
 * @date 13-7-29 下午3:03
 */
public class BMRuntimeException extends RuntimeException {

    public BMRuntimeException(String message) {
        super(message);
    }

    public BMRuntimeException() {
        super();
    }

    public BMRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BMRuntimeException(Throwable cause) {
        super(cause);
    }
}
