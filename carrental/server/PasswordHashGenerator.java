package com.carrental.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generează hash pentru "parola123"
        String password = "parola123";
        String hash = encoder.encode(password);

        System.out.println("===========================================");
        System.out.println("HASH PENTRU PAROLA: " + password);
        System.out.println("===========================================");
        System.out.println(hash);
        System.out.println("===========================================");


        boolean matches = encoder.matches(password, hash);
        System.out.println("\nVERIFICARE: " + (matches ? "✓ CORECT - Hash-ul funcționează!" : " GREȘIT"));


        String oldHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye7DZkgWpFn2L.hU6sNhNJLzUrPnXLJWm";
        boolean oldMatches = encoder.matches(password, oldHash);
        System.out.println("Hash-ul vechi din DB: " + (oldMatches ? " CORECT" : " GREȘIT - Trebuie schimbat!"));

        System.out.println("\n===========================================");
        System.out.println("RULEAZĂ ÎN MYSQL:");
        System.out.println("===========================================");
        System.out.println("USE car_rental;");
        System.out.println("UPDATE users SET password = '" + hash + "'");
        System.out.println("WHERE username IN ('admin', 'manager', 'angajat', 'client');");
        System.out.println("===========================================");
    }
}