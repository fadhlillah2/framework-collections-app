package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.dtos.request.RegisterRequestDTO;
import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.UserResponseDTO;
import com.kbni.backendcode.exceptions.PasswordNotValidException;
import com.kbni.backendcode.exceptions.UsernameFoundException;
import com.kbni.backendcode.repositories.UserRepository;
import com.kbni.backendcode.entities.Users;
import com.kbni.backendcode.exceptions.UserNotFoundException;
import com.kbni.backendcode.services.IAuthService;
import com.kbni.backendcode.utils.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder encoder;

    @Override
    public String generatedToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public UserRequestDTO decodeToken(String token) throws UserNotFoundException {
        String newToken = token.split(" ")[1];

        Jwt jwtToken = jwtDecoder.decode(newToken);

        String data = jwtToken.getSubject();

        Optional<Users> user = userRepository.findUsersByUsername(data);

        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserRequestDTO.class);
        } else {
            log.info("User not found!");
            throw new UserNotFoundException("User not found!");
        }
    }

    @Override
    @Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        Users users = modelMapper.map(registerRequestDTO, Users.class);
        Optional<Users> userUsername = userRepository.findUsersByUsername(users.getUsername());
        if (userUsername.isPresent()) {
            log.info("Username sudah terpakai!");
            throw new UsernameFoundException("Username sudah terpakai");
        }
        if (!PasswordValidator.isValid(users.getPassword())) {
            log.info("Your password must be at least 8 characters including a lowercase letter, an uppercase letter, a symbols, and a number");
            throw new PasswordNotValidException("Your password must be at least 8 characters including a lowercase letter, an uppercase letter, a symbols, and a number");
        }

        users.setPassword(encoder.encode(users.getPassword()));
        Users user1 = userRepository.save(users);
        log.info("Success register user");
        return modelMapper.map(user1, UserResponseDTO.class);
    }

    @Override
    public Users findUser(String username) {
        Optional<Users> user1 = userRepository.findUsersByUsername(username);
        log.info("Success find user");
        return modelMapper.map(user1, Users.class);
    }
}
