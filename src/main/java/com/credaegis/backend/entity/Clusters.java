package com.credaegis.backend.entity;

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
public class Clusters {

    @Id
    private String id;

    private String name;

    private Boolean deactivated;

    @CreationTimestamp
    @Column(name = "created_on",updatable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;


    @OneToOne
    @JoinColumn(name = "admin_id")
    private Users user;

    @OneToMany(mappedBy = "cluster")
    private List<Events> events = new ArrayList<>();

}
