package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

// Ito yung entity para sa mga empleyado na pwedeng mag-assign ng IT assets
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ang ID
    private Long id;

    // Buong pangalan ng empleyado
    @Column(nullable = false)
    private String fullName;

    // Email - ginagamit din bilang username, dapat unique
    @Column(unique = true, nullable = false)
    private String email;

    // Department ng empleyado (hal. "IT", "HR", "Finance")
    @Column(nullable = false)
    private String department;

    // Position o job title ng empleyado
    private String position;

    // Contact number ng empleyado
    private String contactNumber;

    // Role ng user sa system - ADMIN or EMPLOYEE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Status kung active pa ba ang empleyado
    @Column(nullable = false)
    private boolean active = true;

    // Listahan ng mga assets na naka-assign sa user
    // mappedBy = "user" - ibig sabihin ang User ang may-ari ng relationship
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AssetAssignment> assignments;

    // Enum para sa role ng user sa system
    public enum UserRole {
        ADMIN,    // Pwedeng mag-manage ng lahat
        EMPLOYEE  // Regular na empleyado lang
    }
}
