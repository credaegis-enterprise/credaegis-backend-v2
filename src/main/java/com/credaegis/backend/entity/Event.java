package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table (name = "events")
public class Event {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean deactivated = false;


    @Column(nullable = false)
    private String description;


    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Approval> approvals = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    @JsonBackReference
    private User createdBy;


    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    List<Certificate> certificates = new ArrayList<>();


    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp  updatedOn;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @ManyToOne
    @JoinColumn(name = "cluster_id",nullable = false)
    @JsonBackReference
    private Cluster cluster;
}
