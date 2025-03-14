package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventCreationRequest {

    @NotBlank(message = "Event name should not be blank")
    @Size(max = 20, message = "Event name should not exceed 50 characters")
    private String eventName;

    @NotBlank(message = "Description should not be blank")
    @Size(max = 200, message = "Description should not exceed 200 characters")
    private String description;

    @NotBlank(message = "Cluster Id should not be blank")
    private String clusterId;




}
