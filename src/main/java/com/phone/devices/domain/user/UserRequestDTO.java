package com.phone.devices.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO(
        @Email(message = "Username must be a valid email address") String username,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$", message = "Password must contain at least one number, one uppercase letter, and be longer than 8 characters") String password,
        @NotBlank String authority) {}
