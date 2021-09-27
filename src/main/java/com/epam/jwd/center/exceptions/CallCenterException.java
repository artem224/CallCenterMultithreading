package com.epam.jwd.center.exceptions;

public class CallCenterException extends Exception{

    public CallCenterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CallCenterException(String message) {
        super(message);
    }

    public CallCenterException() {
    }
}
