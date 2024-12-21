package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventModificationRequest {


    @NotBlank(message = "Event name should not be empty")
    private  String eventName;

    @NotBlank(message = "Event description should not be empty")
    private  String eventDescription;
}
