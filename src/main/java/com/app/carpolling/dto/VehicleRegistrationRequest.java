package com.app.carpolling.dto;

import com.app.carpolling.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRegistrationRequest {
    
    @NotNull(message = "Driver ID is required")
    private Long driverId;
    
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;
    
    @NotBlank(message = "Brand is required")
    private String brand;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Manufacturing year is required")
    private Integer manufacturingYear;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
    
    @NotNull(message = "Total seats is required")
    private Integer totalSeats;
    
    private Boolean hasAC = true;
    
    private String features;
}









