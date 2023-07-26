package com.kbni.backendcode.exceptions;

public class PasswordNotValidException extends RuntimeException{
    public PasswordNotValidException(String message) {
        super(message);
    }
}
