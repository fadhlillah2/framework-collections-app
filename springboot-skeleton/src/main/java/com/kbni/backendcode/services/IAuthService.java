package com.kbni.backendcode.services;

import com.kbni.backendcode.dtos.request.RegisterRequestDTO;
import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.UserResponseDTO;
import com.kbni.backendcode.entities.Users;
import org.springframework.security.core.Authentication;

public interface IAuthService {

    public String generatedToken(Authentication authentication);

    public UserRequestDTO decodeToken(String token);

    UserResponseDTO registerUser(RegisterRequestDTO registerRequestDTO);

    Users findUser(String username);
}
