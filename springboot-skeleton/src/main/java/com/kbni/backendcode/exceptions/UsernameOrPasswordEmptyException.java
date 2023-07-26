package com.kbni.backendcode.exceptions;

public class UsernameOrPasswordEmptyException extends RuntimeException{
    public UsernameOrPasswordEmptyException(String message) {
        super(message);
    }
}
