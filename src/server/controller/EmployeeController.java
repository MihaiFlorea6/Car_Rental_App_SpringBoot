package com.carrental.server.controller;

import com.carrental.server.model.*;
import com.carrental.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User employee = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<RentalRequest> pendingRentals = rentalService.getPendingRentals();
        List<EmailNotification> emails = emailService.getReceivedEmails(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("pendingRentals", pendingRentals);
        model.addAttribute("emails", emails);

        return "employee";
    }

    // Rental Request Management

    @GetMapping("/api/rentals/pending")
    @ResponseBody
    public ResponseEntity<List<RentalRequest>> getPendingRentals() {
        return ResponseEntity.ok(rentalService.getPendingRentals());
    }

    @PutMapping("/api/rentals/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateRentalStatus(@PathVariable Long id,
                                                @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");

            RentalRequest rental = rentalService.getRentalById(id)
                    .orElseThrow(() -> new RuntimeException("Rental not found"));

            // Actualizare status
            if ("APPROVED".equals(status)) {
                rental = rentalService.approveRental(id);
            } else if ("REJECTED".equals(status)) {
                rental = rentalService.rejectRental(id);
            } else {
                rental.setStatus(status);
                rental = rentalService.updateRentalRequest(id, rental);
            }

            return ResponseEntity.ok(Map.of("success", true, "rental", rental));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Car Status Update

    @PutMapping("/api/cars/{id}/availability")
    @ResponseBody
    public ResponseEntity<?> updateCarAvailability(@PathVariable Long id,
                                                   @RequestBody Map<String, Boolean> request) {
        try {
            Boolean available = request.get("available");
            Car car = carService.updateCarAvailability(id, available);
            return ResponseEntity.ok(Map.of("success", true, "car", car));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/api/cars/{id}/fuel")
    @ResponseBody
    public ResponseEntity<?> updateCarFuelLevel(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> request) {
        try {
            Integer fuelLevel = request.get("fuelLevel");
            Car car = carService.updateCarFuelLevel(id, fuelLevel);
            return ResponseEntity.ok(Map.of("success", true, "car", car));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Client Management

    @GetMapping("/api/clients")
    @ResponseBody
    public ResponseEntity<List<User>> getAllClients() {
        return ResponseEntity.ok(userService.getAllClients());
    }

    // Email

    @GetMapping("/api/emails")
    @ResponseBody
    public ResponseEntity<List<EmailNotification>> getEmails(Authentication authentication) {
        User employee = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return ResponseEntity.ok(emailService.getReceivedEmails(employee.getId()));
    }
}