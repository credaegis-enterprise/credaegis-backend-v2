package com.credaegis.backend.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String pincode;

    @OneToMany(mappedBy =  "organization")
    @JsonManagedReference
    private List<User> users;

    @OneToMany(mappedBy = "organization")
    @JsonManagedReference
    private List<Cluster> clusters;

}
