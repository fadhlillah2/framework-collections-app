package com.kbni.backendcode.controllers;

import com.kbni.backendcode.dtos.response.BaseResponseDTO;
import com.kbni.backendcode.dtos.response.PaginationPeopleSwapiResponseDTO;
import com.kbni.backendcode.dtos.response.ResultDTO;
import com.kbni.backendcode.dtos.response.SwapiResponseDTO;
import com.kbni.backendcode.services.ISwapiDBService;
import com.kbni.backendcode.services.ISwapiService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/swapi")
public class SwapiController {
    private static final String SUCCESS = "Success";
    private final ISwapiService swapiService;
    private final ISwapiDBService swapiDBService;


    public SwapiController(ISwapiService swapiService, ISwapiDBService swapiDBService) {
        this.swapiService = swapiService;
        this.swapiDBService = swapiDBService;
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<BaseResponseDTO<SwapiResponseDTO>> searchPeople(@PathVariable(name = "name") String name) {
        SwapiResponseDTO response = swapiDBService.searchPeopleOnSwapi(name);
        return new ResponseEntity<>(BaseResponseDTO.<SwapiResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Success Get People On Swapi")
                .status(SUCCESS)
                .data(response)
                .build() , HttpStatus.OK);

    }

    @GetMapping("/people/{id}")
    public ResponseEntity<BaseResponseDTO<ResultDTO>> getPeople(@PathVariable(name = "id") Long id) {
        ResultDTO response = swapiDBService.getDetailPeopleByIdUrl(id);
        return new ResponseEntity<>(BaseResponseDTO.<ResultDTO>builder()
                .status(SUCCESS)
                .code(HttpStatus.OK.value())
                .message("Success Get Detail People On Swapi")
                .data(response)
                .build() , HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<BaseResponseDTO<ResultDTO>> addPeople(@RequestBody ResultDTO resultDTO) {
        ResultDTO response = swapiService.addPeople(resultDTO.getId());
        return new ResponseEntity<>(BaseResponseDTO.<ResultDTO>builder()
                .status(SUCCESS)
                .code(HttpStatus.CREATED.value())
                .message("People On Swapi Successfully Created!!")
                .data(response)
                .build(),HttpStatus.CREATED);
    }

    @GetMapping
    public PaginationPeopleSwapiResponseDTO getAllSwapi(@RequestParam(defaultValue = "") String query, Pageable pageable){
        if (pageable.getSort() == Sort.unsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        }

        return swapiService.getAllPeopleSwapi(query, pageable);
    }
}
