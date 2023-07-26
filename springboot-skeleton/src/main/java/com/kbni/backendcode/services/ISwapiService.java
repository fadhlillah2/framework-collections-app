package com.kbni.backendcode.services;

import com.kbni.backendcode.dtos.response.PaginationPeopleSwapiResponseDTO;
import com.kbni.backendcode.dtos.response.ResultDTO;
import org.springframework.data.domain.Pageable;

public interface ISwapiService {

    ResultDTO addPeople(Long id);

    PaginationPeopleSwapiResponseDTO getAllPeopleSwapi(String query, Pageable pageable);
}
