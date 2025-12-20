package com.app.carpolling.service;

import com.app.carpolling.dto.BookingRequest;
import com.app.carpolling.dto.BookingResponse;
import com.app.carpolling.entity.*;
import com.app.carpolling.repository.BookingRepository;
import com.app.carpolling.repository.RoutePointRepository;
import com.app.carpolling.repository.TripSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final TripSeatRepository tripSeatRepository;
    private final RoutePointRepository routePointRepository;
    private final UserService userService;
    private final TripService tripService;
    
    @Transactional
    public Booking createBooking(BookingRequest request) {
        // Get dependencies
        User user = userService.getUserById(request.getUserId());
        Trip trip = tripService.getTripById(request.getTripId());
        
        RoutePoint boardingPoint = routePointRepository.findById(request.getBoardingPointId())
            .orElseThrow(() -> new RuntimeException("Boarding point not found"));
        
        RoutePoint dropPoint = routePointRepository.findById(request.getDropPointId())
            .orElseThrow(() -> new RuntimeException("Drop point not found"));
        
        // Validate trip is scheduled
        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new RuntimeException("Trip is not available for booking");
        }
        
        // Validate seats availability
        if (trip.getAvailableSeats() < request.getSeatNumbers().size()) {
            throw new RuntimeException("Not enough seats available");
        }
        
        // Validate and reserve seats
        for (String seatNumber : request.getSeatNumbers()) {
            TripSeat seat = tripSeatRepository.findByTripIdAndSeatNumber(trip.getId(), seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat " + seatNumber + " not found"));
            
            if (!seat.getIsAvailable()) {
                throw new RuntimeException("Seat " + seatNumber + " is already booked");
            }
            
            // Reserve seat
            seat.setIsAvailable(false);
            tripSeatRepository.save(seat);
        }
        
        // Calculate distance and price
        double distance = (dropPoint.getDistanceFromStart() - boardingPoint.getDistanceFromStart()) / 1000.0;
        double pricePerSeat = distance * trip.getBasePricePerKm();
        double totalAmount = pricePerSeat * request.getSeatNumbers().size();
        
        // Update trip available seats
        trip.setAvailableSeats(trip.getAvailableSeats() - request.getSeatNumbers().size());
        trip.setBookedSeats(trip.getBookedSeats() + request.getSeatNumbers().size());
        
        // Create booking
        Booking booking = new Booking();
        booking.setBookingReference(generateBookingReference());
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setBoardingPoint(boardingPoint);
        booking.setDropPoint(dropPoint);
        booking.setNumberOfSeats(request.getSeatNumbers().size());
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setTotalAmount(totalAmount);
        booking.setDistance(distance);
        booking.setStatus(BookingStatus.PENDING);
        booking.setPassengerNames(request.getPassengerNames());
        booking.setPassengerContacts(request.getPassengerContacts());
        
        return bookingRepository.save(booking);
    }
    
    private String generateBookingReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "BK" + timestamp;
    }
    
    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }
    
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        // Release seats
        for (String seatNumber : booking.getSeatNumbers()) {
            TripSeat seat = tripSeatRepository.findByTripIdAndSeatNumber(
                booking.getTrip().getId(), seatNumber
            ).orElseThrow(() -> new RuntimeException("Seat not found"));
            
            seat.setIsAvailable(true);
            tripSeatRepository.save(seat);
        }
        
        // Update trip available seats
        Trip trip = booking.getTrip();
        trip.setAvailableSeats(trip.getAvailableSeats() + booking.getNumberOfSeats());
        trip.setBookedSeats(trip.getBookedSeats() - booking.getNumberOfSeats());
        
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
    
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream()
            .map(this::convertToBookingResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        return convertToBookingResponse(booking);
    }
    
    private BookingResponse convertToBookingResponse(Booking booking) {
        String vehicleDetails = String.format("%s %s (%s) - %s",
            booking.getTrip().getVehicle().getBrand(),
            booking.getTrip().getVehicle().getModel(),
            booking.getTrip().getVehicle().getColor(),
            booking.getTrip().getVehicle().getRegistrationNumber()
        );
        
        return new BookingResponse(
            booking.getId(),
            booking.getBookingReference(),
            booking.getTrip().getId(),
            booking.getBoardingPoint().getPointName(),
            booking.getDropPoint().getPointName(),
            booking.getTrip().getDepartureTime(),
            booking.getSeatNumbers(),
            booking.getTotalAmount(),
            booking.getStatus(),
            booking.getTrip().getDriver().getUser().getName(),
            booking.getTrip().getDriver().getUser().getPhone(),
            vehicleDetails,
            booking.getCreatedAt()
        );
    }
    
    @Transactional(readOnly = true)
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}









