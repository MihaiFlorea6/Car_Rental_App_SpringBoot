package com.carrental.server.service;

import com.carrental.server.model.*;
import com.carrental.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// ============================================
// CarService - Gestionare Autoturisme
// ============================================
@Service
@Transactional
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car carDetails) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autoturism negăsit!"));

        car.setBrand(carDetails.getBrand());
        car.setModel(carDetails.getModel());
        car.setCategory(carDetails.getCategory());
        car.setYear(carDetails.getYear());
        car.setColor(carDetails.getColor());
        car.setFuelLevel(carDetails.getFuelLevel());
        car.setMaxSpeed(carDetails.getMaxSpeed());
        car.setPricePerDay(carDetails.getPricePerDay());
        car.setAvailable(carDetails.getAvailable());

        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findAllAvailableCars();
    }

    public List<Car> getCarsByCategory(String category) {
        return carRepository.findByCategory(category);
    }

    public Car updateCarAvailability(Long id, Boolean available) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autoturism negăsit!"));
        car.setAvailable(available);
        return carRepository.save(car);
    }

    public Car updateCarFuelLevel(Long id, Integer fuelLevel) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autoturism negăsit!"));
        car.setFuelLevel(fuelLevel);
        return carRepository.save(car);
    }
}