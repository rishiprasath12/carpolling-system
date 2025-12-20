package com.app.carpolling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripCreationRequest {
    
    @NotNull(message = "Route ID is required")
    private Long routeId;
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotNull(message = "Driver ID is required")
    private Long driverId;
    
    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;
    
    @NotNull(message = "Base price per km is required")
    private Double basePricePerKm;
    
    private String specialInstructions;
}









