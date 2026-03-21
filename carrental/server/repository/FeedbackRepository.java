package com.carrental.server.repository;

import com.carrental.server.model.Feedback;
import com.carrental.server.model.User;
import com.carrental.server.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByClient(User client);

    List<Feedback> findByEmployee(User employee);

    List<Feedback> findByCar(Car car);

    @Query("SELECT f FROM Feedback f WHERE f.car.id = :carId")
    List<Feedback> findByCarId(@Param("carId") Long carId);

    // METODĂ NOUĂ - Feedback-uri pentru un client specific
    @Query("SELECT f FROM Feedback f WHERE f.client.id = :clientId")
    List<Feedback> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.car.id = :carId")
    Double findAverageRatingByCarId(@Param("carId") Long carId);

    @Query("SELECT f FROM Feedback f WHERE f.rating >= :minRating")
    List<Feedback> findByMinRating(@Param("minRating") Integer minRating);
}