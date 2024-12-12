package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "approvals")
public class Approval  {

    @Id
    private String id;

    @Column(name = "approval_certificate_id", nullable = false)
    private String approvalCertificateId;

    @Column(name = "approval_certificate_name", nullable = false)
    private String approvalCertificateName;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "expiry_date")
    private Date expiryDate;

    private String  comments;

    @Column(name = "approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference
    private Event event;


    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @CreationTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

}

enum Status{
    pending,
    approved,
    rejected
}
