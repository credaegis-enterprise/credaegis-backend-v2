package com.credaegis.backend.http.response.custom;


import com.credaegis.backend.dto.OrganizationInfoDTO;
import com.credaegis.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfoResponse {


    @JsonProperty("userInfo")
    private User user;

    @JsonProperty("organizationInfo")
    private OrganizationInfoDTO organizationInfoDTO;

}
