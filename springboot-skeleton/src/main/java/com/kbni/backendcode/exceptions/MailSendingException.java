package com.kbni.backendcode.exceptions;

public class MailSendingException extends RuntimeException {
    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
