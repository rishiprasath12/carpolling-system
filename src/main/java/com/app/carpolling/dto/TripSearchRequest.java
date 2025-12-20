package com.app.carpolling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripSearchRequest {
    
    @NotBlank(message = "Boarding point is required")
    private String boardingPoint;
    
    @NotBlank(message = "Drop point is required")
    private String dropPoint;
    
    @NotNull(message = "Travel date is required")
    private LocalDate travelDate;
    
    private Integer requiredSeats = 1;
}









