package com.carrental.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
        System.out.println("Car Rental Application Started Successfully!");
        System.out.println("Access the application at: http://localhost:8080");
    }
}