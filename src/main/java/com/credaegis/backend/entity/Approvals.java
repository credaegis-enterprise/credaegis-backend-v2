package com.credaegis.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "approvals")
public class Approvals {

    @Id
    private String id;

    @Column(name = "approval_certificate_id")
    private String approvalCertificateId;

    @Column(name = "approval_certificate_name")
    private String approvalCertificateName;

    @Column(name = "issued_to_name")
    private String issuedToName;

    @Column(name = "issued_to_email")
    private String issuedToEmail;

    @Column(name = "expiry_date")
    private Date ExpiryDate;

    private String comments;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Events event;


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
