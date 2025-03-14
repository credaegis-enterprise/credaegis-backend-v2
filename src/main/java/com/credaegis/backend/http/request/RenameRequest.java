package com.credaegis.backend.http.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenameRequest {

    @NotBlank(message = "The new name should not be blank")
    @Size(max = 20, message = "The new name should not exceed 50 characters")
    private String newName;
}
