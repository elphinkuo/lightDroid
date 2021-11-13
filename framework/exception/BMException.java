package com.elphin.framework.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author elphin
 * @version 1.0
 * @date 13-7-29 3:02pm
 */
public class BMException extends Exception {
    public BMException(String message) {
        super(message);
    }

    public BMException() {
        super();
    }

    public BMException(String message, Throwable cause) {
        super(message, cause);
    }

    public BMException(Throwable cause) {
        super(cause);
    }
}
