package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificate  {


    @Id
    private String id;

    @Column(name = "certificate_name",nullable = false)
    private String certificateName;

    @Column(name = "certificate_hash" , nullable = false)
    private String certificateHash;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "issued_date", nullable = false)
    private Date issuedDate;

    @Column(name = "expiry_date")
    private Date ExpiryDate;

    @Column(nullable = false)
    private Boolean revoked = false;

    @Column(name = "revoked_date")
    private Date revokedDate;

    private String comments;

    @ManyToOne
    @JoinColumn(name  = "event_id",nullable = false)
    @JsonBackReference
    private Event event;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;



}
