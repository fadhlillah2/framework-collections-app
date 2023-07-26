package com.kbni.backendcode.controllers;

import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.dtos.response.PaginationAllUserResponseDTO;
import com.kbni.backendcode.dtos.response.UserUsernameResponseDTO;
import com.kbni.backendcode.services.IUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    String statusSuccess = "Success";
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<BaseResponseDTO<UserUsernameResponseDTO>> getUsers(@PathVariable("username") String username) {
        return new ResponseEntity<>(BaseResponseDTO.<UserUsernameResponseDTO>builder()
                .status(statusSuccess)
                .message("Successfully get user")
                .code(HttpStatus.OK.value())
                .data(userService.getUser(username))
                .build(), HttpStatus.OK);

    }

    @GetMapping
    public PaginationAllUserResponseDTO getAllUsers(@RequestParam(defaultValue = "") String query, Pageable pageable){
        if (pageable.getSort() == Sort.unsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        }

        return userService.getAllUser(query, pageable);
    }

    @PutMapping("/{username}")
    public ResponseEntity<BaseResponseDTO<UserUsernameResponseDTO>> updateUser(@PathVariable("username") String username, @RequestBody UserRequestDTO userRequestDTO){
        return new ResponseEntity<>(BaseResponseDTO.<UserUsernameResponseDTO>builder()
                .status(statusSuccess)
                .message("User Successfully Updated!!")
                .code(HttpStatus.CREATED.value())
                .data(userService.updateUser(username, userRequestDTO))
                .build(),HttpStatus.CREATED);
    }

}
