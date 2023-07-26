package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.configs.SwapiProperties;
import com.kbni.backendcode.dtos.response.ResultDTO;
import com.kbni.backendcode.dtos.response.SwapiResponseDTO;
import com.kbni.backendcode.services.ISwapiDBService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SwapiDBService implements ISwapiDBService {

    private final RestTemplate restTemplate;
    private final SwapiProperties swapiProperties;

    public SwapiDBService(RestTemplate restTemplate, SwapiProperties swapiProperties) {
        this.restTemplate = restTemplate;
        this.swapiProperties = swapiProperties;
    }


    @Override
    public SwapiResponseDTO searchPeopleOnSwapi(String name) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.putIfAbsent("name", name);
        return restTemplate.getForObject(
                swapiProperties.getUrl() + "/people/?search={name}",
                SwapiResponseDTO.class,
                uriVariables
        );
    }

    @Override
    public ResultDTO getDetailPeopleByIdUrl(Long id) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.putIfAbsent("id", id.toString());

        return restTemplate.getForObject(
                swapiProperties.getUrl() + "/people/{id}",
                ResultDTO.class,
                uriVariables
        );
    }

}
