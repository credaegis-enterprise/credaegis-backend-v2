package com.credaegis.backend.http.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class ApprovalModificationRequest {

    @NotBlank(message = "Approval Id cannot be empty")
    private String approvalId;

    @NotBlank(message = "Recipient Name cannot be empty")
    private String recipientName;

    @NotBlank(message = "Recipient Email cannot be empty")
    private String recipientEmail;


    private String comments;
    private Date expiryDate;

}
