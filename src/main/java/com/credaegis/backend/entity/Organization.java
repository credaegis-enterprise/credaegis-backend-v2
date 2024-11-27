package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table (name = "organizations")
@Data
@NoArgsConstructor
public class Organization {

    @Id
    private String id;

    private String name;

    private String address;

    private String pincode;

    @OneToMany(mappedBy = "organization")
    @JsonManagedReference
    private List<User> users;

}
