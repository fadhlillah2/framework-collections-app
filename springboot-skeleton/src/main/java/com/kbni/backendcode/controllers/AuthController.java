package com.kbni.backendcode.controllers;

import com.kbni.backendcode.dtos.request.LoginRequestDTO;
import com.kbni.backendcode.dtos.request.RegisterRequestDTO;
import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.entities.SpringSession;
import com.kbni.backendcode.exceptions.UsernameOrPasswordEmptyException;
import com.kbni.backendcode.repositories.SpringSessionRepository;
import com.kbni.backendcode.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authServices;

    private final AuthenticationManager authenticationManager;

    @Autowired
    SpringSessionRepository springSessionRepository;

    public AuthController(IAuthService authServices, AuthenticationManager authenticationManager) {
        this.authServices = authServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO<String>> addUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authServices.registerUser(registerRequestDTO);
        return new ResponseEntity<>(BaseResponseDTO.<String>builder()
                .status("Success")
                .message("User successfully created")
                .code(HttpStatus.CREATED.value())
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<String>> token(@RequestBody LoginRequestDTO loginRequestDTO) {

        if (loginRequestDTO.getUsername().isEmpty() || loginRequestDTO.getPassword().isEmpty()) {
            throw new UsernameOrPasswordEmptyException("Username dan / atau password kosong");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        String token = authServices.generatedToken(authentication);

        // Store the authenticated user in the session
        Optional<SpringSession> cekSession = Optional.ofNullable(springSessionRepository.findByPrincipalName(loginRequestDTO.getUsername()));
        if(cekSession.isEmpty()){
            SpringSession springSession = new SpringSession();
            springSession.setPrimaryId(UUID.randomUUID().toString());
            springSession.setSessionId(UUID.randomUUID().toString());
            springSession.setMaxInactiveInterval(55L);
            springSession.setPrincipalName(loginRequestDTO.getUsername());
            springSession.setPrincipalId(UUID.randomUUID().toString());
            springSession.setToken(token);
            springSession.setRefreshToken("");
            springSession.setIsLogin(1);
            springSession.setIsExpired(0);
            springSession.setCreationTime("");
            springSession.setExpiryTime("");
            springSession.setJwtToken(token);

            springSessionRepository.save(springSession);
        }

        return new ResponseEntity<>(BaseResponseDTO.<String>builder()
                .status("Success")
                .message("User successfully login")
                .code(HttpStatus.OK.value())
                .data(token)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserRequestDTO> getUserInfo(@RequestHeader(name ="Authorization") String bearerToken){
        UserRequestDTO userRequestDTO = authServices.decodeToken(bearerToken);
        return new ResponseEntity<>(userRequestDTO, HttpStatus.OK);

    }
}
