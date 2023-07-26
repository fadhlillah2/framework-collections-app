package com.kbni.backendcode.controllers;

import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.dtos.response.PaginationAllUserResponseDTO;
import com.kbni.backendcode.dtos.response.UserUsernameResponseDTO;
import com.kbni.backendcode.services.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    IUserService userService;

    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(
                userService
        );
    }

    @Test
    void getUsers() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);

        given(userService.getUser(anyString()))
                .willReturn(new UserUsernameResponseDTO());

        ResponseEntity<BaseResponseDTO<UserUsernameResponseDTO>> response = userController.getUsers(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    void getAllUsers() {
        Pageable pageable = PageRequest.of(0, 20 , Sort.by("id").descending());
        PaginationAllUserResponseDTO expectedResponse = PaginationAllUserResponseDTO.builder()
                .page(0)
                .size(20)
                .build();
        String query = "test";

        given(userService.getAllUser(query, pageable))
                .willReturn(expectedResponse);

        PaginationAllUserResponseDTO response = userController.getAllUsers(query, pageable);

        assertEquals(expectedResponse, response);
    }

    @Test
    void getAllUserWhenUnsorted(){
        Pageable pageable = PageRequest.of(0, 20 , Sort.by("id").descending());
        PaginationAllUserResponseDTO expectedResponse = PaginationAllUserResponseDTO.builder()
                .page(0)
                .size(20)
                .build();

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        given(userService.getAllUser(anyString(),any(Pageable.class)))
                .willReturn(expectedResponse);

        PaginationAllUserResponseDTO response = userController.getAllUsers("", pageable);

        assertEquals(expectedResponse, response);
    }

    @Test
    void updateUser() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username(username)
                .build();


        given(userService.updateUser(username, requestDTO))
                .willReturn(
                        UserUsernameResponseDTO.builder()
                                .username(username)
                                .build()
                );

        ResponseEntity<BaseResponseDTO<UserUsernameResponseDTO>> response = userController.updateUser(username, requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(username, Objects.requireNonNull(response.getBody()).getData().getUsername());
    }
}