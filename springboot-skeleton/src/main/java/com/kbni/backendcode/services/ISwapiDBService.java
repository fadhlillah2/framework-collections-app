package com.kbni.backendcode.services;

import com.kbni.backendcode.dtos.response.ResultDTO;
import com.kbni.backendcode.dtos.response.SwapiResponseDTO;

public interface ISwapiDBService {

    SwapiResponseDTO searchPeopleOnSwapi(String name);

    ResultDTO getDetailPeopleByIdUrl(Long id);


}
