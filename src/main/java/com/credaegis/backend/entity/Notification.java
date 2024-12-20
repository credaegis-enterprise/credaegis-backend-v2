package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

        @Id
        private String id;

        @Column(nullable = false)
        private String message;

        @Column(nullable = false)
        private String type;

        @Column(name = "timestamp",nullable = false)
        private Timestamp timestamp;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "user_id")
        private User user;

}
