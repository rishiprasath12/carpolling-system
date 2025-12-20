package com.app.carpolling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteCreationRequest {
    
    @NotNull(message = "Driver ID is required")
    private Long driverId;
    
    @NotBlank(message = "Route name is required")
    private String routeName;
    
    @NotNull(message = "Total distance is required")
    private Double totalDistance;
    
    @NotNull(message = "Estimated duration is required")
    private Integer estimatedDuration;
    
    @NotEmpty(message = "Route points are required")
    private List<RoutePointDto> routePoints;
}









