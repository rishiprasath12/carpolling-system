package com.app.carpolling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailabilityResponse {
    private Long tripId;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<SeatInfo> seats;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatInfo {
        private String seatNumber;
        private Boolean isAvailable;
        private Boolean isDriverSeat;
    }
}









