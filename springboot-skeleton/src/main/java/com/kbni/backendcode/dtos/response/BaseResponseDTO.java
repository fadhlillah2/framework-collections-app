package com.kbni.backendcode.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDTO<T> {
    private String status;
    private String message;
    private Integer code;
    private T data;
}
