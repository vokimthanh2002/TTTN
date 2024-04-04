package com.example.demo.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId ;

    private String name ;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;

    // getters and setters


    public Role(Long roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }
}
