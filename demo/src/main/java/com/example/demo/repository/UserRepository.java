package com.example.demo.repository;

import com.example.demo.models.User;
import com.example.demo.models.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository para sa User - automatic na may CRUD operations
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Hanapin ang user gamit ang email (ginagamit din as username)
    Optional<User> findByEmail(String email);

    // Kunin lahat ng users na active pa (hindi pa resigned)
    List<User> findByActive(boolean active);

    // Kunin lahat ng users sa specific na department
    List<User> findByDepartment(String department);

    // Kunin lahat ng users na may specific na role
    List<User> findByRole(UserRole role);

    // Hanapin ang user gamit ang pangalan (partial match, hindi case-sensitive)
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // I-check kung may existing na user na may ganyang email
    boolean existsByEmail(String email);
}
