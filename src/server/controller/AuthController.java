package com.carrental.server.controller;

import com.carrental.server.model.User;
import com.carrental.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        System.out.println("===========================================");
        System.out.println("🔍 Dashboard access pentru: " + username);

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("✓ User role: " + user.getRole());

        model.addAttribute("user", user);

        // Redirecționare bazată pe rol
        switch (user.getRole().toUpperCase()) {
            case "ADMIN":
                System.out.println("➡️ Redirecting to /admin/dashboard");
                return "redirect:/admin/dashboard";
            case "MANAGER":
                System.out.println("➡️ Redirecting to /manager/dashboard");
                return "redirect:/manager/dashboard";
            case "EMPLOYEE":
                System.out.println("➡️ Redirecting to /employee/dashboard");
                return "redirect:/employee/dashboard";
            case "CLIENT":
                System.out.println("➡️ Redirecting to /client/dashboard");
                return "redirect:/client/dashboard";
            default:
                System.out.println("❌ Unknown role, redirecting to login");
                return "redirect:/login";
        }
    }

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (userService.validateUser(username, password)) {
            User user = userService.getUserByUsername(username).get();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("role", user.getRole());
            response.put("username", user.getUsername());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid credentials"));
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            user.setRole("CLIENT");
            user.setEnabled(true);

            userService.createUser(user);

            model.addAttribute("success", "Cont creat cu succes! Te poți autentifica acum.");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> registerApi(@RequestBody User user) {
        try {
            user.setRole("CLIENT");
            user.setEnabled(true);
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(Map.of("success", true, "user", createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}