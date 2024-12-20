package com.credaegis.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotificationMessageDTO {

    private String message;
    private String userId;
    private String type;
    private Timestamp timestamp;
}
