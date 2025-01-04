package com.credaegis.backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class ViewCertificateDTO {

    @NotBlank
    private final InputStream certificateFileStream;

    @NotBlank
    private final String certificateFileName;
}
