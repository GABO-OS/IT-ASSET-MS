package com.example.demo.dto;

import com.example.demo.models.User.UserRole;
import lombok.Data;

// DTO para sa pag-create at pag-update ng User
// Hiwalay sa entity para hindi ma-expose ang sensitive info (hal. password in future)
@Data
public class UserRequest {

    // Required fields
    private String fullName;     // Buong pangalan ng empleyado
    private String email;        // Email address (unique)
    private String department;   // Kung saang department siya
    private UserRole role;       // Kung ADMIN ba siya o EMPLOYEE lang

    // Optional fields
    private String position;       // Job title (hal. "IT Specialist")
    private String contactNumber;  // Telepono
}
