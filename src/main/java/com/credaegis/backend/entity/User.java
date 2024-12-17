package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@Table (name = "users")
public class  User {

    @Id
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mfa_enabled", nullable = false)
    private Boolean mfaEnabled = false;

    @Column(name = "mfa_secret", nullable = true)
    @JsonIgnore
    private String mfaSecret;

    @Column(name = "brand_logo_enabled", nullable = false)
    private Boolean brandLogoEnabled = false;

    @Column(nullable = false)
    private Boolean deactivated = false;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "organization_id",nullable = false)
    @JsonBackReference
    private Organization organization;


    @ManyToOne
    @JoinColumn(name="cluster_id")
    @JsonBackReference
    private Cluster cluster;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private AdminCluster adminCluster;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Role role;


    @OneToMany(mappedBy = "createdBy")
    @JsonProperty("created_events")
    @JsonIgnore
    @JsonManagedReference
    private List<Event> events;

    @OneToMany(mappedBy = "issuedByUser")
    @JsonProperty("issued_certificates")
    @JsonIgnore
    @JsonManagedReference
    private List<Certificate> certificates;



    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


}
