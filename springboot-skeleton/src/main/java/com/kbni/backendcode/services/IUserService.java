package com.kbni.backendcode.services;

import com.kbni.backendcode.dtos.request.UserRequestDTO;
import com.kbni.backendcode.dtos.response.PaginationAllUserResponseDTO;
import com.kbni.backendcode.dtos.response.UserUsernameResponseDTO;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserUsernameResponseDTO updateUser(String username , UserRequestDTO userRequestDTO);
    UserUsernameResponseDTO getUser(String username);

    PaginationAllUserResponseDTO getAllUser(String query, Pageable pageable);
}
