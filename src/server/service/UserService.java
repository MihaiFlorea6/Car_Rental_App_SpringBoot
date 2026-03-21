package com.carrental.server.service;

import com.carrental.server.model.User;
import com.carrental.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creare utilizator NOU - CRIPTEAZĂ parola automat cu BCrypt
     */
    public User createUser(User user) {
        // Verificăm dacă username-ul există deja
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username-ul există deja!");
        }

        // IMPORTANT: Criptăm parola cu BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salvăm utilizatorul în baza de date
        return userRepository.save(user);
    }

    /**
     * Actualizare utilizator existent
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizator negăsit!"));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setEnabled(userDetails.getEnabled());

        // Actualizăm parola DOAR dacă este furnizată una nouă
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * Ștergere utilizator
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Obținere utilizator după ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Obținere utilizator după username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Obținere toți utilizatorii
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obținere utilizatori după rol
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    /**
     * Obținere toți angajații
     */
    public List<User> getAllEmployees() {
        return userRepository.findAllEmployees();
    }

    /**
     * Obținere toți clienții
     */
    public List<User> getAllClients() {
        return userRepository.findAllClients();
    }

    /**
     * Schimbă statusul utilizatorului (activ/inactiv)
     */
    public User toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizator negăsit!"));
        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }

    /**
     * Validare credențiale (pentru Socket Server)
     */
    public boolean validateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Verificăm dacă contul este activ și parola este corectă
        return user.getEnabled() && passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Obținere utilizator autentificat (folosit în controllere)
     */
    public User getAuthenticatedUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizator negăsit!"));
    }
}