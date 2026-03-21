package com.carrental.server.repository;

import com.carrental.server.model.EmployeeAttribute;
import com.carrental.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeAttributeRepository extends JpaRepository<EmployeeAttribute, Long> {
    List<EmployeeAttribute> findByEmployee(User employee);

    @Query("SELECT ea FROM EmployeeAttribute ea WHERE ea.employee.id = :employeeId")
    List<EmployeeAttribute> findByEmployeeId(@Param("employeeId") Long employeeId);
}