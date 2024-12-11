package com.credaegis.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
@Table(name = "admins")
public class AdminCluster implements Serializable {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "admin_id",nullable = false)
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "cluster_id",nullable = false)
    @JsonBackReference
    private Cluster cluster;

    @UpdateTimestamp
    @Column(name = "last_modified")
    private Timestamp lastModified;


}
