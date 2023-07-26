package com.kbni.backendcode.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationAllUserResponseDTO implements Serializable {
    private String status;
    private String message;
    private Integer code;
    private List<UserUsernameResponseDTO> data;
    private Integer page;
    private Integer size;
    @JsonProperty("total_page")
    private Integer totalPage;
    @JsonProperty("total_item")
    private Long totalItem;
}
