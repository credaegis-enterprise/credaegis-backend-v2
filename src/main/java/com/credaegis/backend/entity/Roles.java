package com.credaegis.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Roles {


    @Id
    private String id;

    private String authority;

    private String role;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;


}
