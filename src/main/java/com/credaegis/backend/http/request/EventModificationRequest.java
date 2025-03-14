package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventModificationRequest {


    @NotBlank(message = "Event name should not be empty")
    @Size(max = 20, message = "Event name should not exceed 50 characters")
    private  String eventName;

    @NotBlank(message = "Event description should not be empty")
    @Size(max = 200, message = "Event description should not exceed 200 characters")
    private  String eventDescription;
}
