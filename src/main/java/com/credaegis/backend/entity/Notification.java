package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

        @Id
        private String id;

        private String message;


        private String type;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "user_id")
        private User user;

}
