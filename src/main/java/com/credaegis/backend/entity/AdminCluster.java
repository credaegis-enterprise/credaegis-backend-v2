package com.credaegis.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
@Table(name = "admins")
public class AdminCluster {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "admin_id",nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "cluster_id",nullable = false)
    private Cluster cluster;

    @UpdateTimestamp
    @Column(name = "last_modified")
    private Timestamp lastModified;


}
