package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationStatisticDTO {

    private String name;
    private Long clusterCount;
    private Long eventCount;
    private Long MemberCount;
}
