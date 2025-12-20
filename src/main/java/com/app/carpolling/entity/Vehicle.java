package com.app.carpolling.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;
    
    @Column(nullable = false, unique = true)
    private String registrationNumber;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private String model;
    
    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false)
    private Integer manufacturingYear;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType; // SEDAN, SUV, HATCHBACK, etc.
    
    @Column(nullable = false)
    private Integer totalSeats; // Including driver seat
    
    @Column(nullable = false)
    private Integer passengerSeats; // Excluding driver seat
    
    @Column(nullable = false)
    private Boolean hasAC = true;
    
    @Column(columnDefinition = "TEXT")
    private String features; // JSON or comma-separated features
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}




