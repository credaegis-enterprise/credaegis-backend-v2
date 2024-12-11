package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {


    @Id
    private String id;

    @Column(nullable = false)
    private String role;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User user;


}
