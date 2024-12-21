package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

        @Id
        private String id;

        @Column(nullable = false)
        private String message;

        @Column(nullable = false)
        private NotificationType type;

        @Column(name = "timestamp",nullable = false)
        private Timestamp timestamp;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "user_id")
        private User user;

}
