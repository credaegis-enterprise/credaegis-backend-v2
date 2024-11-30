package com.credaegis.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaInfoDTO {

    @NotBlank(message = "File name cannot be blank")
    private String fileName;

    @NotBlank(message = "Name of recipient cannot be empty")
    private String recipientName;

    @NotBlank(message = "Email of Recipient should not be empty")
    private String recipientEmail;

}
