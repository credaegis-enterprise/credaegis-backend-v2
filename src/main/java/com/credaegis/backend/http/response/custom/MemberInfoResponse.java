package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.projection.MemberInfoProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponse {

    private List<MemberInfoProjection> members;
    private String clusterAdminName;
    private String clusterAdminEmail;
    private String clusterName;
    private String clusterId;
}
