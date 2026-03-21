package com.carrental.server.repository;

import com.carrental.server.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// ============================================
// UserRepository
// ============================================
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByRole(String role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.enabled = true")
    List<User> findActiveUsersByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.role = 'EMPLOYEE'")
    List<User> findAllEmployees();

    @Query("SELECT u FROM User u WHERE u.role = 'CLIENT'")
    List<User> findAllClients();
}
