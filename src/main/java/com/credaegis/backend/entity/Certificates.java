package com.credaegis.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;

    @CreationTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;



}
