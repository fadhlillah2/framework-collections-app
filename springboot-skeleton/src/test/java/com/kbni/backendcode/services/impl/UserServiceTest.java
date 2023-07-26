package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.repositories.UserRepository;
import com.kbni.backendcode.dtos.response.PaginationAllUserResponseDTO;
import com.kbni.backendcode.dtos.response.UserUsernameResponseDTO;
import com.kbni.backendcode.entities.Users;
import com.kbni.backendcode.exceptions.UserNotFoundException;
import com.kbni.backendcode.services.IUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    IUserService userService;

    UserService userService1;

    UserDetailsService userDetailsService;


    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, new ModelMapper());
    }



    @Test
    void updateUser() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        Users users = Users.builder()
                .username(username)
                .password("Password@1234")
                .build();

        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("test")
                .password("Password@12345")
                .build();

        given(userRepository.findUsersByUsername(username))
                .willReturn(Optional.of(users));
        given(userRepository.save(users))
                .willReturn(users);

        UserUsernameResponseDTO responseDTO = userService.updateUser(username, requestDTO);

        assertEquals(requestDTO.getUsername(), responseDTO.getUsername());
    }

    @Test
    void getUser() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);
        Users users = Users.builder()
                .username(username)
                .build();

        given(userRepository.findUsersByUsername(username))
                .willReturn(Optional.of(users));

        UserUsernameResponseDTO responseDTO = userService.getUser(username);

        assertEquals(username, responseDTO.getUsername());
    }

    @Test
    void getUserFailed() {
        String username = RandomStringUtils.randomAlphabetic(5, 10);

        given(userRepository.findUsersByUsername(username))
                .willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(username)
        );
    }

    @Test
    void getAllUser() {
        Pageable pageable = Pageable.ofSize(10);
        String query = "test";

        Users users = new Users();
        users.setUsername("test");
        List<Users> usersList = new ArrayList<>(List.of(users));


        UserUsernameResponseDTO userUsernameResponseDTO = new UserUsernameResponseDTO();
        userUsernameResponseDTO.setUsername("test");

        Page<Users> userPage = new PageImpl<>(usersList, pageable, pageable.getPageSize());

        given(userRepository.findAllUser(query, pageable))
                .willReturn(userPage);

        PaginationAllUserResponseDTO responseDTO = userService.getAllUser(query, pageable);

        assertEquals("Success", responseDTO.getStatus());
        assertEquals(10, responseDTO.getSize());
    }


}