package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller para sa User/Employee management
// Lahat ng empleyado-related na API endpoints ay nandito
@RestController
@RequestMapping("/api/users")  // Base URL para sa user endpoints
@RequiredArgsConstructor
@CrossOrigin(origins = "*")    // Tanggapin ang requests mula sa kahit saang origin
public class UserController {

    private final UserService userService;

    // POST /api/users - Mag-register ng bagong empleyado
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    // GET /api/users - Kunin lahat ng users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/active - Kunin ang mga active users lang
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    // GET /api/users/{id} - Kunin ang isang specific na user
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET /api/users/search?name=juan - Maghanap ng user gamit ang pangalan
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchUsers(name));
    }

    // GET /api/users/department/{dept} - Kunin lahat ng users sa isang department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(userService.getUsersByDepartment(department));
    }

    // PUT /api/users/{id} - I-update ang impormasyon ng empleyado
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // PATCH /api/users/{id}/deactivate - I-deactivate ang user (soft delete)
    // Mas maganda ang soft delete para hindi mawala ang history ng assignments
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    // DELETE /api/users/{id} - Permanently tanggalin ang user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Matagumpay na natanggal ang user!");
    }
}
