package com.carrental.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "rentalRequests", "feedbacks"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(length = 50)
    private String category;

    private Integer year;

    @Column(length = 30)
    private String color;

    @Column(name = "fuel_level")
    private Integer fuelLevel;

    @Column(name = "max_speed")
    private Integer maxSpeed;

    @Column(name = "price_per_day")
    private Double pricePerDay;

    @Column(nullable = false)
    private Boolean available = true;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<RentalRequest> rentalRequests = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks = new HashSet<>();

    public Car(String brand, String model, String category, Integer year,
               String color, Integer fuelLevel, Integer maxSpeed,
               Double pricePerDay, Boolean available) {
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.year = year;
        this.color = color;
        this.fuelLevel = fuelLevel;
        this.maxSpeed = maxSpeed;
        this.pricePerDay = pricePerDay;
        this.available = available;
    }
}
