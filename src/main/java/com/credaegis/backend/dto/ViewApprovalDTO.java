package com.credaegis.backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;


@Data
@AllArgsConstructor
public class ViewApprovalDTO {


    @NotBlank
    private final InputStream approvalFileStream;

    @NotBlank
    private final String approvalFileName;

}
