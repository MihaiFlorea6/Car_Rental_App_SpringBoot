package com.carrental.server.repository;

import com.carrental.server.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    List<RentalRequest> findByClient(User client);
    List<RentalRequest> findByCar(Car car);
    List<RentalRequest> findByStatus(String status);

    @Query("SELECT r FROM RentalRequest r WHERE r.client.id = :clientId")
    List<RentalRequest> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT r FROM RentalRequest r WHERE r.car.id = :carId")
    List<RentalRequest> findByCarId(@Param("carId") Long carId);

    @Query("SELECT r FROM RentalRequest r WHERE r.status = 'PENDING'")
    List<RentalRequest> findAllPendingRequests();

    @Query("SELECT r FROM RentalRequest r WHERE r.status = 'APPROVED'")
    List<RentalRequest> findAllApprovedRequests();

    @Query("SELECT r FROM RentalRequest r WHERE r.car.id = :carId AND " +
            "((r.startDate <= :endDate AND r.endDate >= :startDate)) AND " +
            "r.status IN ('APPROVED', 'PENDING')")
    List<RentalRequest> findConflictingRentals(@Param("carId") Long carId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}

// ============================================
// EmployeeAttributeRepository
// ============================================
