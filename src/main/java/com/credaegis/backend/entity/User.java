package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;


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
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "mfa_enabled", nullable = false)
    private Boolean mfaEnabled = false;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(nullable = false)
    private Boolean deactivated = false;

    @ManyToOne
    @JoinColumn(name = "organization_id",nullable = false)
    @JsonBackReference
    private Organization organization;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Role role;

    @OneToOne(mappedBy = "admin")
    @JsonManagedReference
    private Cluster cluster;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


}
