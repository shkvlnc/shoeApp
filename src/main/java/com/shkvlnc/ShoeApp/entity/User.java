package com.shkvlnc.ShoeApp.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Oracle supports IDENTITY in 12c+
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // e.g. "ADMIN", "CUSTOMER"

    // getters and setters
}
