package com.carrental.server.service;

import com.carrental.server.model.RentalRequest;
import com.carrental.server.model.Car;
import com.carrental.server.model.User;
import com.carrental.server.repository.RentalRequestRepository;
import com.carrental.server.repository.CarRepository;
import com.carrental.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalService {

    @Autowired
    private RentalRequestRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Metodă pentru Client Dashboard - cu parametri separați și date
     */
    public RentalRequest createRentalWithDates(Long clientId, Long carId, LocalDate startDate, LocalDate endDate) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getAvailable()) {
            throw new RuntimeException("❌ Mașina nu este disponibilă momentan");
        }

        // Verificare conflicte cu mesaj detaliat
        List<RentalRequest> conflicts = rentalRepository.findConflictingRentals(
                carId, startDate, endDate);

        if (!conflicts.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringBuilder message = new StringBuilder(
                    "❌ Mașina " + car.getBrand() + " " + car.getModel() +
                            " este deja rezervată în următoarele perioade:\n\n");

            for (RentalRequest conflict : conflicts) {
                message.append("📅 De la ")
                        .append(conflict.getStartDate().format(formatter))
                        .append(" până la ")
                        .append(conflict.getEndDate().format(formatter))
                        .append(" (Status: ").append(conflict.getStatus()).append(")\n");
            }

            message.append("\n💡 Te rugăm să alegi alte date sau o altă mașină.");

            throw new RuntimeException(message.toString());
        }

        RentalRequest newRequest = new RentalRequest();
        newRequest.setClient(client);
        newRequest.setCar(car);
        newRequest.setStartDate(startDate);
        newRequest.setEndDate(endDate);
        newRequest.setStatus("PENDING");
        newRequest.setDiscountApplied(0.0);

        return rentalRepository.save(newRequest);
    }

    /**
     * Metodă pentru SocketServer - fără date (folosește default)
     */
    public RentalRequest createRentalForSocket(Long clientId, Long carId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getAvailable()) {
            throw new RuntimeException("❌ Mașina nu este disponibilă");
        }

        // Default: de azi până mâine
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        // Verificare conflicte
        List<RentalRequest> conflicts = rentalRepository.findConflictingRentals(
                carId, startDate, endDate);

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("❌ Mașina este deja rezervată pentru perioada solicitată!");
        }

        RentalRequest newRequest = new RentalRequest();
        newRequest.setClient(client);
        newRequest.setCar(car);
        newRequest.setStartDate(startDate);
        newRequest.setEndDate(endDate);
        newRequest.setStatus("PENDING");
        newRequest.setDiscountApplied(0.0);

        return rentalRepository.save(newRequest);
    }

    /**
     * Metodă generică - primește obiect RentalRequest complet
     */
    public RentalRequest createRentalRequest(RentalRequest request) {
        Car car = carRepository.findById(request.getCar().getId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getAvailable()) {
            throw new RuntimeException("❌ Mașina nu este disponibilă");
        }

        // Verificare conflicte de intervale
        List<RentalRequest> conflicts = rentalRepository.findConflictingRentals(
                car.getId(), request.getStartDate(), request.getEndDate());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("❌ Mașina este deja rezervată pentru acest interval! Alege alte date.");
        }

        request.setStatus("PENDING");
        return rentalRepository.save(request);
    }

    public RentalRequest updateRentalRequest(Long id, RentalRequest requestDetails) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        request.setStartDate(requestDetails.getStartDate());
        request.setEndDate(requestDetails.getEndDate());
        request.setStatus(requestDetails.getStatus());
        request.setDiscountApplied(requestDetails.getDiscountApplied());

        return rentalRepository.save(request);
    }

    public RentalRequest approveRental(Long id) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        request.setStatus("APPROVED");

        // Update car availability
        Car car = request.getCar();
        car.setAvailable(false);
        carRepository.save(car);

        return rentalRepository.save(request);
    }

    public RentalRequest rejectRental(Long id) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        request.setStatus("REJECTED");
        return rentalRepository.save(request);
    }

    public RentalRequest completeRental(Long id) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        request.setStatus("COMPLETED");

        // Make car available again
        Car car = request.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return rentalRepository.save(request);
    }

    public void deleteRentalRequest(Long id) {
        rentalRepository.deleteById(id);
    }

    public Optional<RentalRequest> getRentalById(Long id) {
        return rentalRepository.findById(id);
    }

    public List<RentalRequest> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<RentalRequest> getRentalsByClientId(Long clientId) {
        return rentalRepository.findByClientId(clientId);
    }

    public List<RentalRequest> getRentalsByCarId(Long carId) {
        return rentalRepository.findByCarId(carId);
    }

    public List<RentalRequest> getPendingRentals() {
        return rentalRepository.findAllPendingRequests();
    }

    public List<RentalRequest> getApprovedRentals() {
        return rentalRepository.findAllApprovedRequests();
    }

    public RentalRequest applyDiscount(Long id, Double discount) {
        RentalRequest request = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        request.setDiscountApplied(discount);
        return rentalRepository.save(request);
    }
}