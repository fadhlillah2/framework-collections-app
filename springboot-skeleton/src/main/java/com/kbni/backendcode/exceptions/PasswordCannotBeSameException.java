package com.kbni.backendcode.exceptions;

public class PasswordCannotBeSameException extends RuntimeException{
    public PasswordCannotBeSameException(String message) {
        super(message);
    }
}
