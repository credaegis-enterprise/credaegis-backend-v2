package com.credaegis.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "event_name")
    private String eventName;

    private Boolean deactivated;


    @OneToMany(mappedBy = "event")
    List<Approval> approvals = new ArrayList<>();


    @OneToMany(mappedBy = "event")
    List<Certificate> certificates = new ArrayList<>();


    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;
}
