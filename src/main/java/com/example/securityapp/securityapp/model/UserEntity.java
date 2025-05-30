package com.example.securityapp.securityapp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/*
*
* A secure entity to store user credentials
✅ Role information stored per user in a separate table (user_roles)
✅ JPA repository to query user by username
✅ Password is never stored as plain text
✅ Roles are eagerly loaded — suitable for authorization decisions
* */
@Entity
//@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // BCrypt hash

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name= "user_id"))
    @Column(name= "role")
    private Set<String> roles = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
