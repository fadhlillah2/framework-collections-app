package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.PaginationAllUserResponseDTO;
import com.kbni.backendcode.dtos.response.UserUsernameResponseDTO;
import com.kbni.backendcode.entities.Users;
import com.kbni.backendcode.exceptions.PasswordCannotBeSameException;
import com.kbni.backendcode.exceptions.UserNotFoundException;
import com.kbni.backendcode.exceptions.UsernameFoundException;
import com.kbni.backendcode.exceptions.UsernameOrPasswordEmptyException;
import com.kbni.backendcode.repositories.UserRepository;
import com.kbni.backendcode.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class UserService implements IUserService , UserDetailsService {

    private final  ModelMapper modelMapper;
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private static final String MESSAGE = "User Not Found!";

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Users> user = userRepository.findUsersByUsername(username);
        if(user.isEmpty()) {
            log.info(MESSAGE);
            throw new UserNotFoundException(MESSAGE);
        }
        log.info("Success find user");
        return user.get();
    }

    @Override
    public UserUsernameResponseDTO updateUser(String username, UserRequestDTO userRequestDTO) {
        Optional<Users> optionalUsers = userRepository.findUsersByUsername(username);
        Optional<Users> checkUsername = userRepository.findUsersByUsername(userRequestDTO.getUsername());

        if (userRequestDTO.getUsername().isEmpty() || userRequestDTO.getPassword().isEmpty()) {
            log.info("Username dan / atau password kosong");
            throw new UsernameOrPasswordEmptyException("Username dan / atau password kosong");
        }

        if (optionalUsers.isEmpty()) {
            log.info(MESSAGE);
            throw new UserNotFoundException(MESSAGE);
        }
        Users updateUser = optionalUsers.get();

        if (checkUsername.isPresent() && !username.equals(userRequestDTO.getUsername()) ){
            log.info("Username sudah terpakai!");
            throw new UsernameFoundException("Username sudah terpakai!");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(userRequestDTO.getPassword(), updateUser.getPassword());

        if (isPasswordMatch) {
            log.info("Password tidak boleh sama dengan password sebelumnya");
            throw new PasswordCannotBeSameException("Password tidak boleh sama dengan password sebelumnya");
        }

        userRequestDTO.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        modelMapper.map(userRequestDTO, updateUser);
        Users newUser = userRepository.save(updateUser);
        log.info("Success update user");
        return modelMapper.map(newUser, UserUsernameResponseDTO.class);

    }

    @Override
    @Transactional
    public UserUsernameResponseDTO getUser(String username) {
        Optional<Users> user1 = userRepository.findUsersByUsername(username);
        if(user1.isEmpty()){
            log.info(MESSAGE);
            throw new UserNotFoundException(MESSAGE);
        }
        log.info("Success get user");
        return modelMapper.map(user1, UserUsernameResponseDTO.class);
    }


    @Override
    public PaginationAllUserResponseDTO getAllUser(String query, Pageable pageable) {
        Page<Users> usersPage = userRepository.findAllUser(query, pageable);
        List<UserUsernameResponseDTO> userUsernameResponseDTOList = new ArrayList<>();
        for (Users users : usersPage.getContent()) {
            UserUsernameResponseDTO travelResponseDTO = modelMapper.map(users, UserUsernameResponseDTO.class);
            userUsernameResponseDTOList.add(travelResponseDTO);
        }
        log.info("Successfully get all users");
        return PaginationAllUserResponseDTO.builder()
                .status("Success")
                .message("Successfully get all users")
                .code(HttpStatus.OK.value())
                .data(userUsernameResponseDTOList)
                .totalItem(usersPage.getTotalElements())
                .totalPage(usersPage.getTotalPages())
                .page(usersPage.getNumber())
                .size(usersPage.getSize())
                .build();
    }
}
