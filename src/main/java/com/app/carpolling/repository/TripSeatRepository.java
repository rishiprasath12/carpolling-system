package com.app.carpolling.repository;

import com.app.carpolling.entity.TripSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripSeatRepository extends JpaRepository<TripSeat, Long> {
    List<TripSeat> findByTripId(Long tripId);
    List<TripSeat> findByTripIdAndIsAvailableTrue(Long tripId);
    Optional<TripSeat> findByTripIdAndSeatNumber(Long tripId, String seatNumber);
}









