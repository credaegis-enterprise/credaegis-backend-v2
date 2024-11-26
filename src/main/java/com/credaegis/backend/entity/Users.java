package com.credaegis.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table (name = "users")
public class Users {

    @Id
    private String id;

    private String username;

    private String password;

    private String email;

    @OneToMany(mappedBy = "users")
    private List<Roles> roles = new ArrayList<>();

}
