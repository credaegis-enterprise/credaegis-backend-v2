package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "batch_info")
@Data
public class BatchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "merkle_root", nullable = false)
    private String merkleRoot;

    @Column(name = "hash_count", nullable = false)
    private Integer hashCount;


    @Column(name = "push_time", nullable = true)
    private Timestamp pushTime;


    @Column(name = "txn_hash", nullable = true)
    private String txnHash;


    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn;


    @OneToMany(mappedBy = "batchInfo")
    @JsonManagedReference
    private List<Certificate> certificates;


}
