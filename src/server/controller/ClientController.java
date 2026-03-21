package com.carrental.server.controller;

import com.carrental.server.model.*;
import com.carrental.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();

        User client = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // ✅ FIX: Obține TOATE mașinile, nu doar cele disponibile
        List<Car> allCars = carService.getAllCars();
        List<RentalRequest> myRentals = rentalService.getRentalsByClientId(client.getId());

        // ✅ ADAUGĂ NOTIFICĂRILE PRIMITE DE LA MANAGER
        List<EmailNotification> notifications = emailService.getReceivedEmails(client.getId());

        // ✅ ADAUGĂ FEEDBACK-URILE PRIMITE
        List<Feedback> feedbacksReceived = feedbackService.getFeedbackByClientId(client.getId());

        model.addAttribute("user", client);
        model.addAttribute("cars", allCars);  // ✅ TOATE mașinile
        model.addAttribute("requests", myRentals);
        model.addAttribute("notifications", notifications);
        model.addAttribute("feedbacksReceived", feedbacksReceived);

        return "client";
    }

    @PostMapping("/rent/{carId}")
    public String rentCar(
            @PathVariable Long carId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication,
            Model model) {

        try {
            String username = authentication.getName();
            User client = userService.getUserByUsername(username).orElseThrow();

            // Validare date
            if (endDate.isBefore(startDate)) {
                return "redirect:/client/dashboard?error=dates";
            }

            if (startDate.isBefore(LocalDate.now())) {
                return "redirect:/client/dashboard?error=past";
            }

            // Folosește metoda corectă cu date
            rentalService.createRentalWithDates(client.getId(), carId, startDate, endDate);

            return "redirect:/client/dashboard?success=true";
        } catch (Exception e) {
            System.err.println("Eroare la rezervare: " + e.getMessage());
            return "redirect:/client/dashboard?error=conflict";
        }
    }
}