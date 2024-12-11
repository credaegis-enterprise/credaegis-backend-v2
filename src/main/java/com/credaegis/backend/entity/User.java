package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class  User implements Serializable {

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

    @Column(name = "profile_url", unique = true)
    private String profileUrl;

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
    @JsonManagedReference
    private List<Event> events;



    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


}
