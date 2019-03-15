package com.telleroo;

public class TellerooException extends RuntimeException {

    public TellerooException(String message) {
        super(message);
    }

    public TellerooException(Throwable e) {
        super(e);
    }
}
