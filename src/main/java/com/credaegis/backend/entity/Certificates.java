package com.credaegis.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
public class Certificates {


    @Id
    private String id;

    @Column(name = "certificate_name")
    private String certificateName;

    @Column(name = "certificate_hash")
    private String certificateHash;

    @Column(name = "issued_to_name")
    private String issuedToName;

    @Column(name = "issued_to_email")
    private String issuedToEmail;

    @Column(name = "issued_date")
    private Date issuedDate;

    @Column(name = "expiry_date")
    private Date ExpiryDate;

    private Boolean revoked;

    @Column(name = "revoked_date")
    private Date revokedDate;

    private String comments;

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
