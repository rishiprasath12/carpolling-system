package com.app.carpolling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityWithLocationsDto {
    private String city;
    private List<SubLocationDto> subLocations;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubLocationDto {
        private Long id;
        private String subLocation;
        private String fullName; // "City - SubLocation"
        private String address;
    }
}









