package com.carrental.server.controller;

import com.carrental.server.model.*;
import com.carrental.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    /**
     * Client trimite feedback către Manager despre o mașină
     */
    @PostMapping("/client-to-manager")
    public ResponseEntity<?> clientToManager(@RequestBody Map<String, Object> request,
                                             Authentication authentication) {
        try {
            // Obține clientul curent
            User client = userService.getUserByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            // Obține datele din request
            String message = request.get("message").toString();
            Integer rating = Integer.valueOf(request.get("rating").toString());
            Long carId = Long.valueOf(request.get("carId").toString());

            // Obține mașina
            Car car = carService.getCarById(carId)
                    .orElseThrow(() -> new RuntimeException("Car not found"));

            // Creează feedback-ul
            // Employee va fi null pentru feedback de la client către manager
            Feedback feedback = new Feedback();
            feedback.setClient(client);
            feedback.setEmployee(null); // Nu specificăm angajatul
            feedback.setCar(car);
            feedback.setMessage(message);
            feedback.setRating(rating);

            Feedback savedFeedback = feedbackService.createFeedback(feedback);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Feedback trimis cu succes!",
                    "feedback", savedFeedback
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Eroare la trimiterea feedback-ului: " + e.getMessage()
            ));
        }
    }

    /**
     * Manager trimite feedback către Client
     */
    @PostMapping("/manager-to-client")
    public ResponseEntity<?> managerToClient(@RequestBody Map<String, Object> request,
                                             Authentication authentication) {
        try {
            // Obține managerul curent
            User manager = userService.getUserByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            // Verifică că este manager
            if (!"MANAGER".equals(manager.getRole())) {
                throw new RuntimeException("Doar managerii pot trimite feedback către clienți!");
            }

            // Obține datele din request
            Long clientId = Long.valueOf(request.get("clientId").toString());
            String message = request.get("message").toString();
            Integer rating = Integer.valueOf(request.get("rating").toString());

            // Obține clientul
            User client = userService.getUserById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            // Creează feedback-ul (fără mașină specificată, e feedback general)
            Feedback feedback = new Feedback();
            feedback.setClient(client);
            feedback.setEmployee(manager); // Managerul e "angajatul" care trimite feedback
            feedback.setCar(null); // Feedback general, nu despre o mașină specifică
            feedback.setMessage(message);
            feedback.setRating(rating);

            Feedback savedFeedback = feedbackService.createFeedback(feedback);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Feedback trimis cu succes către client!",
                    "feedback", savedFeedback
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Eroare: " + e.getMessage()
            ));
        }
    }

    /**
     * Obține toate feedback-urile pentru o mașină
     */
    @GetMapping("/car/{carId}")
    public ResponseEntity<?> getFeedbackForCar(@PathVariable Long carId) {
        try {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "feedbacks", feedbackService.getFeedbackByCarId(carId)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}