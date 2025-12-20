package com.app.carpolling.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trip_seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"trip_id", "seatNumber"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "trip"})
public class TripSeat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;
    
    @Column(nullable = false)
    private String seatNumber; // e.g., "S1", "S2", "S3"
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    @Column(nullable = false)
    private Boolean isDriverSeat = false;
}




