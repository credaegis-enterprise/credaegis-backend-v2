package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {


    @Id
    private String id;

    private String authority;

    private String role;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


}
