package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "batch_info")
@Data
public class BatchInfo {

    @Id
    private Integer id;

    @Column(name = "merkle_root", nullable = true)
    private String merkleRoot;

    @Column(name = "hash_count", nullable = true)
    private Integer hashCount;


    @Column(name = "push_time", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp pushTime;


    @Column(name = "txn_hash", nullable = true)
    private String txnHash;


    @Column(name = "txn_fee",nullable = true)
    private String txnFee;

    @Column(name = "push_status",nullable = false)
    private Boolean pushStatus = false;


    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Timestamp updatedOn;


    @OneToMany(mappedBy = "batchInfo")
    @JsonIgnore
    private List<Certificate> certificates;


}