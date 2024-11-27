package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table (name = "clusters")
public class Cluster {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Boolean deactivated = false;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "organization_id",nullable = false)
    private Organization organization;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "admin_id",nullable = false)
    private User admin;

    @OneToMany(mappedBy = "cluster")
    @JsonManagedReference
    private List<Event> events = new ArrayList<>();

}
