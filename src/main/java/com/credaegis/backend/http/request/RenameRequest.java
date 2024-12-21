package com.credaegis.backend.http.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenameRequest {

    @NotBlank(message = "The new name should not be blank")
    private String newName;
}
