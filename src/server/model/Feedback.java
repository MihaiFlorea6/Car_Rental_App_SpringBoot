package com.carrental.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"password", "attributes", "rentalRequests", "feedbacks"})
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties({"password", "attributes", "rentalRequests", "feedbacks"})
    private User employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    @JsonIgnoreProperties({"rentalRequests", "feedbacks"})
    private Car car;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Integer rating; // 1-5

    public Feedback(User client, User employee, Car car, String message, Integer rating) {
        this.client = client;
        this.employee = employee;
        this.car = car;
        this.message = message;
        this.rating = rating;
    }
}