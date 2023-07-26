package com.kbni.backendcode.exceptions;

public class UsernameFoundException extends RuntimeException{
    public UsernameFoundException(String message) {
        super(message);
    }
}
