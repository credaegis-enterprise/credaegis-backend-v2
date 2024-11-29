package com.credaegis.backend.dto.response.custom.api;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomApiResponse <T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String message;
    private Boolean success;


}
