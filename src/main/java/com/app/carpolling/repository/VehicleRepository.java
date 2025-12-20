package com.app.carpolling.repository;

import com.app.carpolling.entity.Vehicle;
import com.app.carpolling.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByDriver(Driver driver);
    List<Vehicle> findByDriverIdAndIsActiveTrue(Long driverId);
    boolean existsByRegistrationNumber(String registrationNumber);
}









