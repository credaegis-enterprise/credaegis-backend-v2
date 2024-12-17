package com.credaegis.backend.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoDTO {
    private String id;
    private String username;
    private String email;
    private Boolean mfaEnabled;
    private String profileUrl;
    private Boolean deactivated;
    private Boolean deleted;
}
