package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventModificationRequest {


    @NotBlank(message = "Event name should not be empty")
    private final String eventName;

    @NotBlank(message = "Event description should not be empty")
    private final String eventDescription;
}
