package com.example.demo.dto;

import com.example.demo.models.User.UserRole;

// DTO para sa pag-create at pag-update ng User
public class UserRequest {

    private String fullName;
    private String email;
    private String department;
    private UserRole role;
    private String position;
    private String contactNumber;

    // Getters and Setters - Manual naming para sigurado
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
