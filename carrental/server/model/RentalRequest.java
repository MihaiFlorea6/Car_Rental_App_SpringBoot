package com.carrental.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rental_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RentalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"password", "attributes", "rentalRequests", "feedbacks"})
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    @JsonIgnoreProperties({"rentalRequests", "feedbacks"})
    private Car car;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 20)
    private String status;

    @Column(name = "discount_applied")
    private Double discountApplied = 0.0;

    public RentalRequest(User client, Car car, LocalDate startDate,
                         LocalDate endDate, String status, Double discountApplied) {
        this.client = client;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.discountApplied = discountApplied;
    }
}
