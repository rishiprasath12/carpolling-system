package com.app.carpolling.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Driver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(nullable = false, unique = true)
    private String licenseNumber;
    
    @Column(nullable = false)
    private String licenseExpiryDate;
    
    @Column(nullable = false)
    private Integer experienceYears;
    
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;
    
    @Column(nullable = false)
    private Boolean isVerified = false;
    
    @Column(nullable = false)
    private Double rating = 0.0;
    
    @Column(nullable = false)
    private Integer totalTrips = 0;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}




