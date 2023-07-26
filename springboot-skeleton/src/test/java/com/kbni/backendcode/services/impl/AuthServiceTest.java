package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.configs.RSAKeyProperties;
import com.kbni.backendcode.dtos.request.RegisterRequestDTO;
import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.repositories.UserRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.kbni.backendcode.dtos.response.UserResponseDTO;
import com.kbni.backendcode.entities.Users;
import com.kbni.backendcode.exceptions.UserNotFoundException;
import com.kbni.backendcode.exceptions.UsernameFoundException;
import com.kbni.backendcode.services.IAuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    JwtDecoder jwtDecoder;

    JwtEncoder jwtEncoder;

    ModelMapper modelMapper = new ModelMapper();

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    IAuthService authServices;

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File publicPemFile = new File(Objects.requireNonNull(classLoader.getResource("certs/public.pem")).getFile());
        File privatePemFile = new File(Objects.requireNonNull(classLoader.getResource("certs/private.pem")).getFile());

        RSAPublicKey rsaPublicKey = readPublicKey(publicPemFile);
        RSAPrivateKey rsaPrivateKey = readPrivateKey(privatePemFile);

        RSAKeyProperties rsaKeyProperties = new RSAKeyProperties();
        rsaKeyProperties.setPublicKey(rsaPublicKey);
        rsaKeyProperties.setPrivateKey(rsaPrivateKey);

        JWK jwk = new RSAKey.Builder(rsaKeyProperties.getPublicKey()).privateKey(rsaKeyProperties.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        jwtEncoder = new NimbusJwtEncoder(jwks);
        jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey()).build();
        authServices = new AuthService(
                jwtEncoder,
                jwtDecoder,
                userRepository,
                modelMapper,
                passwordEncoder
        );
    }

    public RSAPublicKey readPublicKey(File file) throws IOException {
        try (FileReader keyReader = new FileReader(file)) {
            PEMParser pemParser = new PEMParser(keyReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            return (RSAPublicKey) converter.getPublicKey(publicKeyInfo);
        }
    }

    public RSAPrivateKey readPrivateKey(File file) throws IOException {
        try (FileReader keyReader = new FileReader(file)) {

            PEMParser pemParser = new PEMParser(keyReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());

            return (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
        }
    }


    @Test
    void generatedToken() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        Principal principal = () -> username;
        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        String generatedToken = authServices.generatedToken(authentication);

        String generatedTokenUsername = jwtDecoder.decode(generatedToken).getSubject();
        assertEquals(username, generatedTokenUsername);
    }

    @Test
    void decodeToken() {
        String bearerToken = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidGVzdCIsImV4cCI6MTY3NTU5NTg2NiwiaWF0IjoxNjc1NDIzMDY2LCJzY29wZSI6IiJ9.B_io3NATWPqXHtsRq7pFzDRYy19mBd5XTa3W7sh74OKuMmZnZ1XeOuko1iczNSFGNZ2Ztj3CKzgBXrFZ4sbPUfCwC-oCLp_wI-XXYWXxUqSUE0HdRR-RA7MBpP_sq4c2rxbbmoNi6910gLSkL0Sh8EXJgBSun6fYDpBvqSn2JeZcYV2JEizasblu4fhE-aKhJ-NkhMUaxKAmouZj65A-vG3Eedr4cygiEcxvoKu79hK9e7fn7W9PvYg7AByDh_qslhNbm9xYd2iC8PQ7YV_kZ1A0cZaBBPLdvoT-iRDvIYzg2QQbkJLXKIxPJ4qujCAD9tO4nIJkbE8KA8RmmBNzGQ";
        String username = "test";
        Users users = Users.builder()
                .username(username)
                .build();

        given(userRepository.findUsersByUsername(username))
                .willReturn(Optional.of(users));

        UserRequestDTO responseDTO = authServices.decodeToken(bearerToken);

        assertEquals(username, responseDTO.getUsername());
    }

    @Test
    void decodeTokenFailed(){
        String bearerToken = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidGVzdCIsImV4cCI6MTY3NTU5NTg2NiwiaWF0IjoxNjc1NDIzMDY2LCJzY29wZSI6IiJ9.B_io3NATWPqXHtsRq7pFzDRYy19mBd5XTa3W7sh74OKuMmZnZ1XeOuko1iczNSFGNZ2Ztj3CKzgBXrFZ4sbPUfCwC-oCLp_wI-XXYWXxUqSUE0HdRR-RA7MBpP_sq4c2rxbbmoNi6910gLSkL0Sh8EXJgBSun6fYDpBvqSn2JeZcYV2JEizasblu4fhE-aKhJ-NkhMUaxKAmouZj65A-vG3Eedr4cygiEcxvoKu79hK9e7fn7W9PvYg7AByDh_qslhNbm9xYd2iC8PQ7YV_kZ1A0cZaBBPLdvoT-iRDvIYzg2QQbkJLXKIxPJ4qujCAD9tO4nIJkbE8KA8RmmBNzGQ";
        given(userRepository.findUsersByUsername(anyString()))
                .willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> authServices.decodeToken(bearerToken)
        );
    }

    @Test
    void registerUser() {
        RegisterRequestDTO requestDTO = RegisterRequestDTO.builder()
                .username("satrio.ramadhan280")
                .password("Password@80")
                .build();
        Users user = new ModelMapper().map(requestDTO, Users.class);

        given(userRepository.findUsersByUsername(requestDTO.getUsername()))
                .willReturn(Optional.empty());
        given(userRepository.save(any()))
                .willReturn(user);

        UserResponseDTO responseDTO = authServices.registerUser(requestDTO);
        assertEquals(requestDTO.getUsername(), responseDTO.getUsername());
    }

    @Test
    void registerUserUsernameExist() {
        RegisterRequestDTO requestDTO = RegisterRequestDTO.builder()
                .username("satrio.ramadhan280")
                .password("Password@80")
                .build();
        Users user = new ModelMapper().map(requestDTO, Users.class);

        given(userRepository.findUsersByUsername(requestDTO.getUsername()))
                .willReturn(Optional.of(user));

        assertThrows(
                UsernameFoundException.class,
                () -> authServices.registerUser(requestDTO)
        );

    }

    @Test
    void findUser() {
        Users users = new Users();
        users.setUsername("test");
        given(userRepository.findUsersByUsername(users.getUsername()))
                .willReturn(Optional.of(users));

        Users user = authServices.findUser(users.getUsername());

        assertEquals(users.getUsername() , user.getUsername());
    }
}