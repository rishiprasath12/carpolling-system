package com.app.carpolling.dto;

import com.app.carpolling.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email; // Optional
    
    @NotBlank(message = "Phone is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be 10 digits"
    )
    private String phone;
    
    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
        message = "Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character"
    )
    private String password;
    
    @NotNull(message = "Role is required")
    private UserRole role;
}




