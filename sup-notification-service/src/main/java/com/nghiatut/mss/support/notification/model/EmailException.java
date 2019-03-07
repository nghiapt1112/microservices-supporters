package com.nghiatut.mss.support.notification.model;

public class EmailException extends RuntimeException {

    public EmailException(String code, String message) {
        super(code + ": " + message);
    }

    public EmailException(String code, String message, Throwable cause) {
        super(code + ": " + message, cause);
    }
}
