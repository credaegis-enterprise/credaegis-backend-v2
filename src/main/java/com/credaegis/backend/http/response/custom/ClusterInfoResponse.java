package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.AdminClusterInfoDTO;
import com.credaegis.backend.dto.ClusterInfoDTO;
import com.credaegis.backend.dto.EventInfoDTO;
import com.credaegis.backend.dto.MemberInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class ClusterInfoResponse {

    private ClusterInfoDTO clusterInfo;
    private AdminClusterInfoDTO adminInfo;
    private List<EventInfoDTO> events;
    private List<MemberInfoDTO> members;


}
