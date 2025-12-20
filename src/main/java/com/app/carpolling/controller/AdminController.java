package com.app.carpolling.controller;

import com.app.carpolling.dto.AdminDashboardStats;
import com.app.carpolling.dto.ApiResponse;
import com.app.carpolling.entity.*;
import com.app.carpolling.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {
    
    private final AdminService adminService;
    
    // ==================== DASHBOARD ====================
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<AdminDashboardStats>> getDashboardStats() {
        try {
            AdminDashboardStats stats = adminService.getDashboardStats();
            return ResponseEntity.ok(
                ApiResponse.success("Dashboard stats retrieved successfully", stats)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== USER MANAGEMENT ====================
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully", users)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<ApiResponse<User>> activateUser(@PathVariable Long userId) {
        try {
            User user = adminService.activateUser(userId);
            return ResponseEntity.ok(
                ApiResponse.success("User activated successfully", user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<User>> deactivateUser(@PathVariable Long userId) {
        try {
            User user = adminService.deactivateUser(userId);
            return ResponseEntity.ok(
                ApiResponse.success("User deactivated successfully", user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== DRIVER MANAGEMENT ====================
    
    @GetMapping("/drivers")
    public ResponseEntity<ApiResponse<List<Driver>>> getAllDrivers() {
        try {
            List<Driver> drivers = adminService.getAllDrivers();
            return ResponseEntity.ok(
                ApiResponse.success("Drivers retrieved successfully", drivers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/drivers/unverified")
    public ResponseEntity<ApiResponse<List<Driver>>> getUnverifiedDrivers() {
        try {
            List<Driver> drivers = adminService.getUnverifiedDrivers();
            return ResponseEntity.ok(
                ApiResponse.success("Unverified drivers retrieved successfully", drivers)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/drivers/{driverId}/verify")
    public ResponseEntity<ApiResponse<Driver>> verifyDriver(@PathVariable Long driverId) {
        try {
            Driver driver = adminService.verifyDriver(driverId);
            return ResponseEntity.ok(
                ApiResponse.success("Driver verified successfully", driver)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/drivers/{driverId}/unverify")
    public ResponseEntity<ApiResponse<Driver>> unverifyDriver(@PathVariable Long driverId) {
        try {
            Driver driver = adminService.unverifyDriver(driverId);
            return ResponseEntity.ok(
                ApiResponse.success("Driver verification revoked successfully", driver)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== VEHICLE MANAGEMENT ====================
    
    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<Vehicle>>> getAllVehicles() {
        try {
            List<Vehicle> vehicles = adminService.getAllVehicles();
            return ResponseEntity.ok(
                ApiResponse.success("Vehicles retrieved successfully", vehicles)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== TRIP MANAGEMENT ====================
    
    @GetMapping("/trips")
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTrips() {
        try {
            List<Trip> trips = adminService.getAllTrips();
            return ResponseEntity.ok(
                ApiResponse.success("Trips retrieved successfully", trips)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== BOOKING MANAGEMENT ====================
    
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        try {
            List<Booking> bookings = adminService.getAllBookings();
            return ResponseEntity.ok(
                ApiResponse.success("Bookings retrieved successfully", bookings)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}







