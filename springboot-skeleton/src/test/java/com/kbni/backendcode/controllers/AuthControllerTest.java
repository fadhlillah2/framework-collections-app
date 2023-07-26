package com.kbni.backendcode.controllers;

import com.kbni.backendcode.dtos.request.LoginRequestDTO;
import com.kbni.backendcode.dtos.request.RegisterRequestDTO;
import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.exceptions.UsernameOrPasswordEmptyException;
import com.kbni.backendcode.services.IAuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    IAuthService authServices;

    @Mock
    AuthenticationManager authenticationManager;

    AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(
                authServices,
                authenticationManager
        );
    }

    @Test
    void addUser() {
        RegisterRequestDTO requestDTO = RegisterRequestDTO.builder()
                .username("test")
                .build();

        ResponseEntity<BaseResponseDTO<String>> response = authController.addUser(requestDTO);
        then(authServices)
                .should()
                .registerUser(requestDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void token() {
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("test")
                .password("Password@1234")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword());
        String token = RandomStringUtils.randomAlphabetic(5, 20);

        given(authenticationManager.authenticate(authentication))
                .willReturn(authentication);

        given(authServices.generatedToken(authentication))
                .willReturn(token);


        ResponseEntity<BaseResponseDTO<String>> response = authController.token(requestDTO);

        assertEquals(token, Objects.requireNonNull(response.getBody()).getData());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserInfo() {
        String token = RandomStringUtils.randomAlphabetic(5, 20);
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("test")
                .build();

        given(authServices.decodeToken(token))
                .willReturn(requestDTO);

        ResponseEntity<UserRequestDTO> response = authController.getUserInfo(token);

        assertEquals(requestDTO.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void tokenFailed() {
        LoginRequestDTO requestDTO = LoginRequestDTO.builder()
                .username("")
                .password("")
                .build();

        assertThrows(
                UsernameOrPasswordEmptyException.class,
                () -> authController.token(requestDTO)
        );
    }
}