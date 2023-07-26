package com.kbni.backendcode.handler;

import com.kbni.backendcode.exceptions.*;
import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;


import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleUser() {
        String message = "User not found";
        ResponseEntity<Object> response = exceptionHandler.handleUser(new UserNotFoundException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void handleUsername() {
        String message = "Username sudah terpakai!";
        ResponseEntity<Object> response = exceptionHandler.handleUsername(new UsernameFoundException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handlePassword() {
        String message = "Your password must be at least 8 characters including a lowercase letter, an uppercase letter, a symbols, and a number";
        ResponseEntity<Object> response = exceptionHandler.handlePassword(new PasswordNotValidException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handlePasswordCannotBeSame() {
        String message = "Password tidak boleh sama dengan password sebelumnya";
        ResponseEntity<Object> response = exceptionHandler.handlePasswordCannotBeSame(new PasswordCannotBeSameException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleUsernameOrPasswordEmpty() {
        String message = "Username dan / atau password kosong";
        ResponseEntity<Object> response = exceptionHandler.handleUsernameOrPasswordEmpty(new UsernameOrPasswordEmptyException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleEmailOrPassword() {
        String message = "Wrong email or password!";
        ResponseEntity<Object> response = exceptionHandler.handleEmailOrPassword(new BadCredentialsException(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void handleGlobal() {
        String message = "There is an error in the server";
        ResponseEntity<Object> response = exceptionHandler.handleGlobal(new Exception(message));
        assertEquals(message, ((BaseResponseDTO) Objects.requireNonNull(response.getBody())).getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}