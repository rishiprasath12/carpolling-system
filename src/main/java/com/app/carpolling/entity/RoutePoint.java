package com.app.carpolling.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "route_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "route"})
public class RoutePoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    
    @Column(nullable = false)
    private String city; // e.g., "Bangalore", "Mumbai"
    
    @Column(nullable = false)
    private String subLocation; // e.g., "Electronic City", "Silk Board"
    
    @Column(nullable = false)
    private String pointName; // Combined: "Bangalore - Electronic City"
    
    @Column(nullable = false)
    private String address; // Full address
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    private Integer sequenceOrder; // 1, 2, 3... to maintain order
    
    @Column(nullable = false)
    private Integer distanceFromStart; // in meters from starting point
    
    @Column(nullable = false)
    private Integer timeFromStart; // in minutes from starting point
    
    @Column(nullable = false)
    private Boolean isBoardingPoint = true;
    
    @Column(nullable = false)
    private Boolean isDropPoint = true;
}

