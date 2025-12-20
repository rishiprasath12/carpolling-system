package com.app.carpolling.dto;

import com.app.carpolling.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String bookingReference;
    private Long tripId;
    private String boardingPoint;
    private String dropPoint;
    private LocalDateTime departureTime;
    private List<String> seatNumbers;
    private Double totalAmount;
    private BookingStatus status;
    private String driverName;
    private String driverPhone;
    private String vehicleDetails;
    private LocalDateTime bookedAt;
}









