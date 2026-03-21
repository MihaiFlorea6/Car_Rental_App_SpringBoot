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
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private CarService carService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeAttributeService attributeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Car> cars = carService.getAllCars();
        List<RentalRequest> rentals = rentalService.getAllRentals();
        List<User> employees = userService.getAllEmployees();
        List<User> clients = userService.getAllClients();
        List<Feedback> feedbacks = feedbackService.getAllFeedback();

        model.addAttribute("cars", cars);
        model.addAttribute("rentals", rentals);
        model.addAttribute("employees", employees);
        model.addAttribute("clients", clients);
        model.addAttribute("feedbacks", feedbacks);

        return "manager";
    }

    // ============================================
    // CAR MANAGEMENT
    // ============================================

    @GetMapping("/api/cars")
    @ResponseBody
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/api/cars")
    @ResponseBody
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        try {
            Car createdCar = carService.createCar(car);
            return ResponseEntity.ok(Map.of("success", true, "car", createdCar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/api/cars/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCar(@PathVariable Long id, @RequestBody Car car) {
        try {
            Car updatedCar = carService.updateCar(id, car);
            return ResponseEntity.ok(Map.of("success", true, "car", updatedCar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/api/cars/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Car deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/api/cars/{id}/toggle-availability")
    @ResponseBody
    public ResponseEntity<?> toggleCarAvailability(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id)
                    .orElseThrow(() -> new RuntimeException("Car not found"));

            // Schimbăm disponibilitatea
            car.setAvailable(!car.getAvailable());
            Car updatedCar = carService.updateCar(id, car);

            return ResponseEntity.ok(Map.of("success", true, "car", updatedCar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ============================================
    // RENTAL MANAGEMENT
    // ============================================

    @GetMapping("/api/rentals")
    @ResponseBody
    public ResponseEntity<List<RentalRequest>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    // ============================================
    // EMPLOYEE MANAGEMENT
    // ============================================

    @GetMapping("/api/employees")
    @ResponseBody
    public ResponseEntity<List<User>> getAllEmployees() {
        return ResponseEntity.ok(userService.getAllEmployees());
    }

    /**
     * ENDPOINT NOU - Adăugare angajat
     */
    @PostMapping("/api/employees")
    @ResponseBody
    public ResponseEntity<?> addEmployee(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");

            // Verificare username existent
            if (userService.getUserByUsername(username).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Username-ul există deja!"));
            }

            // Creare utilizator nou cu rol EMPLOYEE
            User employee = new User();
            employee.setUsername(username);
            employee.setEmail(email);
            employee.setPassword(password); // Va fi criptat automat în UserService
            employee.setRole("EMPLOYEE");
            employee.setEnabled(true);

            User created = userService.createUser(employee);

            return ResponseEntity.ok(Map.of("success", true, "user", created));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * Ștergere angajat
     */
    @DeleteMapping("/api/employees/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Employee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ============================================
    // EMPLOYEE ATTRIBUTES MANAGEMENT
    // ============================================

    @GetMapping("/api/employees/{id}/attributes")
    @ResponseBody
    public ResponseEntity<List<EmployeeAttribute>> getEmployeeAttributes(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributesByEmployeeId(id));
    }

    @PostMapping("/api/employees/{id}/attributes")
    @ResponseBody
    public ResponseEntity<?> addEmployeeAttribute(@PathVariable Long id,
                                                  @RequestBody Map<String, String> request) {
        try {
            String attributeName = request.get("attributeName");
            EmployeeAttribute attribute = attributeService.addAttribute(id, attributeName);
            return ResponseEntity.ok(Map.of("success", true, "attribute", attribute));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/api/attributes/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAttribute(@PathVariable Long id) {
        try {
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Attribute deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ============================================
    // EMAIL MANAGEMENT
    // ============================================

    @PostMapping("/api/send-email")
    @ResponseBody
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> request,
                                       Authentication authentication) {
        try {
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String subject = request.get("subject").toString();
            String message = request.get("message").toString();

            User sender = userService.getUserByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            User receiver = userService.getUserById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            EmailNotification notification = emailService.sendEmail(sender, receiver, subject, message);
            return ResponseEntity.ok(Map.of("success", true, "notification", notification));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}