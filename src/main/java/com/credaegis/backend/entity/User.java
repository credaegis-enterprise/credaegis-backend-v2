package com.credaegis.backend.entity;

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
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private String email;

    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled = false;

    @Column(name = "profile_url")
    private String profileUrl;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Role role;

    @OneToOne(mappedBy = "user")
    private Cluster cluster;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


}
