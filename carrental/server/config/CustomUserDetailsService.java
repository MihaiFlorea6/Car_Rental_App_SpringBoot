package com.carrental.server.config;

import com.carrental.server.model.User;
import com.carrental.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("===========================================");
        System.out.println(" Încercare autentificare pentru: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println(" User-ul NU a fost găsit în baza de date!");
                    return new UsernameNotFoundException("User not found: " + username);
                });

        System.out.println("✓ User găsit: " + user.getUsername());
        System.out.println("✓ Rol: " + user.getRole());
        System.out.println("✓ Enabled: " + user.getEnabled());
        System.out.println("✓ Password hash din DB: " + user.getPassword().substring(0, 20) + "...");

        // IMPORTANT: Prefixul ROLE_ este obligatoriu pentru hasRole()
        String roleWithPrefix = "ROLE_" + user.getRole().toUpperCase();
        System.out.println("✓ Rol cu prefix: " + roleWithPrefix);
        System.out.println("===========================================");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix)))
                .disabled(!user.getEnabled())
                .build();
    }
}