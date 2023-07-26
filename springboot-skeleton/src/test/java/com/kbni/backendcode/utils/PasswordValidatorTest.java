package com.kbni.backendcode.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void isValid() {
        String password = "Password@1234";
        assertTrue(PasswordValidator.isValid(password));
    }

    @Test
    void isValidFailed(){
        String password = "pass";
        assertFalse(PasswordValidator.isValid(password));
    }
}