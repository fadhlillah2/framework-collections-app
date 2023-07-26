package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.dtos.response.PaginationPeopleSwapiResponseDTO;
import com.kbni.backendcode.dtos.response.ResultDTO;
import com.kbni.backendcode.entities.Swapis;
import com.kbni.backendcode.repositories.SwapiRepository;
import com.kbni.backendcode.services.ISwapiService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SwapiService implements ISwapiService {

    private final SwapiRepository swapiRepository;
    private final SwapiDBService swapiDBService;
    private final ModelMapper modelMapper;

    public SwapiService(SwapiRepository swapiRepository, SwapiDBService swapiDBService, ModelMapper modelMapper) {
        this.swapiRepository = swapiRepository;
        this.swapiDBService = swapiDBService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResultDTO addPeople(Long id) {
        ResultDTO resultDTO = swapiDBService.getDetailPeopleByIdUrl(id);
        Swapis swapis = modelMapper.map(resultDTO , Swapis.class);
        swapis.setId(id);
        Swapis swapis1 = swapiRepository.save(swapis);
        log.info("Success Add People From Swapi");
        return modelMapper.map(swapis1, ResultDTO.class);
    }

    @Override
    public PaginationPeopleSwapiResponseDTO getAllPeopleSwapi(String query, Pageable pageable) {
        Page<Swapis> swapisPage = swapiRepository.findAllSwapi(query, pageable);
        List<ResultDTO> resultDTOList = new ArrayList<>();
        for (Swapis swapis : swapisPage.getContent()) {
            ResultDTO resultDTO = modelMapper.map(swapis, ResultDTO.class);
            resultDTOList.add(resultDTO);
        }
        log.info("Successfully get all poeple from swapi");
        return PaginationPeopleSwapiResponseDTO.builder()
                .status("Success")
                .message("Successfully get all from swapi")
                .code(HttpStatus.OK.value())
                .data(resultDTOList)
                .totalItem(swapisPage.getTotalElements())
                .totalPage(swapisPage.getTotalPages())
                .page(swapisPage.getNumber())
                .size(swapisPage.getSize())
                .build();
    }
}
