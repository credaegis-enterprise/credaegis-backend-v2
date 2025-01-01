package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.projection.CertificateInfoProjection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CertificateListResponse {

    private Long count;

    @JsonProperty("certificates")
    private List<CertificateInfoProjection> certificateInfoProjection;
}
