package com.carrental.server.repository;

import com.carrental.server.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByAvailable(Boolean available);

    List<Car> findByCategory(String category);

    List<Car> findByBrand(String brand);

    @Query("SELECT c FROM Car c WHERE c.available = true")
    List<Car> findAllAvailableCars();

    @Query("SELECT c FROM Car c WHERE c.category = :category AND c.available = true")
    List<Car> findAvailableCarsByCategory(@Param("category") String category);

    @Query("SELECT c FROM Car c WHERE c.pricePerDay <= :maxPrice AND c.available = true")
    List<Car> findAvailableCarsByMaxPrice(@Param("maxPrice") Double maxPrice);

    @Query("SELECT DISTINCT c.category FROM Car c WHERE c.category IS NOT NULL")
    List<String> findAllCategories();

    @Query("SELECT c FROM Car c WHERE c.brand = :brand AND c.model = :model")
    List<Car> findByBrandAndModel(@Param("brand") String brand, @Param("model") String model);
}