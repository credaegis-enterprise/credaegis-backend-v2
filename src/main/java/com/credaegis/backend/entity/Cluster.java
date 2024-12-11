package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table (name = "clusters")
public class Cluster implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Boolean deactivated = false;


    @Column(nullable = false)
    private Boolean locked = false; //disable privileges of cluster adminCluster to a normal user.

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization organization;

    @OneToOne(mappedBy = "cluster")
    @JsonManagedReference
    private AdminCluster adminCluster;


    @OneToMany(mappedBy = "cluster")
    @JsonManagedReference
    @JsonProperty("members")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "cluster")
    @JsonManagedReference
    private List<Event> events = new ArrayList<>();

}
