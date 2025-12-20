package com.app.carpolling.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Trip ID is required")
    private Long tripId;
    
    @NotNull(message = "Boarding point ID is required")
    private Long boardingPointId;
    
    @NotNull(message = "Drop point ID is required")
    private Long dropPointId;
    
    @NotEmpty(message = "Seat numbers are required")
    private List<String> seatNumbers;
    
    private String passengerNames;
    
    private String passengerContacts;
}









