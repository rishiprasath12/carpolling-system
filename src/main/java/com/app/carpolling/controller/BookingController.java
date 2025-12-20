package com.app.carpolling.controller;

import com.app.carpolling.dto.ApiResponse;
import com.app.carpolling.dto.BookingRequest;
import com.app.carpolling.dto.BookingResponse;
import com.app.carpolling.entity.Booking;
import com.app.carpolling.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(
        @Valid @RequestBody BookingRequest request
    ) {
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(
        @PathVariable Long userId
    ) {
        try {
            List<BookingResponse> bookings = bookingService.getUserBookings(userId);
            return ResponseEntity.ok(
                ApiResponse.success("Bookings retrieved successfully", bookings)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByReference(
        @PathVariable String bookingReference
    ) {
        try {
            BookingResponse booking = bookingService.getBookingByReference(bookingReference);
            return ResponseEntity.ok(
                ApiResponse.success("Booking retrieved successfully", booking)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
        @PathVariable Long bookingId
    ) {
        try {
            Booking booking = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(
                ApiResponse.success("Booking cancelled successfully", booking)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}









