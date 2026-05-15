package com.example.demo.services;

import com.example.demo.dto.UserRequest;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Service para sa User management - pag-manage ng mga empleyado
@Service
@RequiredArgsConstructor
public class UserService {

    // I-inject ang repository para makausap ang database
    private final UserRepository userRepository;

    // ===================== CREATE =====================

    // Mag-register ng bagong empleyado sa system
    public User createUser(UserRequest request) {
        // I-check muna kung may ganyang email na - hindi pwedeng duplicate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("May existing na user na may email na: " + request.getEmail());
        }

        // Gumawa ng bagong User object
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setContactNumber(request.getContactNumber());
        user.setRole(request.getRole());
        user.setActive(true); // Default active ang bagong user

        return userRepository.save(user);
    }

    // ===================== READ =====================

    // Kunin lahat ng users (para sa listahan ng empleyado)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Kunin lahat ng active users lang
    public List<User> getActiveUsers() {
        return userRepository.findByActive(true);
    }

    // Hanapin ang isang user gamit ang ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang user na may ID: " + id));
    }

    // Hanapin ang user gamit ang email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Hindi mahanap ang user na may email: " + email));
    }

    // Maghanap ng user gamit ang pangalan (partial search)
    public List<User> searchUsers(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }

    // Kunin lahat ng users sa specific na department
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }

    // ===================== UPDATE =====================

    // I-update ang impormasyon ng empleyado
    public User updateUser(Long id, UserRequest request) {
        User user = getUserById(id);

        // I-check kung may ibang user na may ganyang email
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("May ibang user na na may email na: " + request.getEmail());
        }

        // I-update ang mga fields
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setContactNumber(request.getContactNumber());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    // I-deactivate ang empleyado (soft delete - hindi tinatanggal, basta inactive lang)
    // Mas maganda ito kaysa tanggalin kasi may kasaysayan pa rin ng assignments
    public User deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }

    // ===================== DELETE =====================

    // Hard delete - ganap na tatanggalin sa database
    // Mag-ingat kapag gagamit nito - baka may assignments pa ang user!
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
