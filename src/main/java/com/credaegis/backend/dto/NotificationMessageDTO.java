package com.credaegis.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class NotificationMessageDTO {

    private String message;
    private String userId;
    private String type;
    private Timestamp timestamp;
}
