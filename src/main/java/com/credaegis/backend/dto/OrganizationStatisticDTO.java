package com.credaegis.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationStatisticDTO {

    private String name;
    private Long clusterCount;
    private Long eventCount;
    private Long MemberCount;
}
