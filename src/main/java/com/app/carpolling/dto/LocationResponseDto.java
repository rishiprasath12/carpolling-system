package com.app.carpolling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private Long id;
    private String city;
    private String subLocation;
    private String pointName; // Combined: "City - SubLocation"
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isBoardingPoint;
    private Boolean isDropPoint;
}









