package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EventCreationRequest {

    @NotBlank(message = "Event name should not be blank")
    private String eventName;

    @NotBlank(message = "Description should not be blank")
    private String description;

    @NotBlank(message = "Cluster Id should not be blank")
    private String clusterId;




}
