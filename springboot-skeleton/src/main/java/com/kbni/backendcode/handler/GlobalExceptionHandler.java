package com.kbni.backendcode.handler;

import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    String failed = "Failed";
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUser(UserNotFoundException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message(e.getMessage()).status(failed).code(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UsernameFoundException.class})
    public ResponseEntity<Object> handleUsername(UsernameFoundException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message(e.getMessage()).status(failed).code(HttpStatus.CONFLICT.value()).build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {PasswordNotValidException.class})
    public ResponseEntity<Object> handlePassword(PasswordNotValidException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message(e.getMessage()).status(failed).code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PasswordCannotBeSameException.class})
    public ResponseEntity<Object> handlePasswordCannotBeSame(PasswordCannotBeSameException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message(e.getMessage()).status(failed).code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameOrPasswordEmptyException.class})
    public ResponseEntity<Object> handleUsernameOrPasswordEmpty(UsernameOrPasswordEmptyException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message(e.getMessage()).status(failed).code(HttpStatus.BAD_REQUEST.value()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleEmailOrPassword(BadCredentialsException e) {
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message("Wrong email or password!").status(failed).code(HttpStatus.UNAUTHORIZED.value()).build(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGlobal(Exception e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(BaseResponseDTO.builder()
                .message("There is an error in the server").status(failed).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
