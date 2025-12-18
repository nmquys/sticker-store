package com.nmquys.springbootstore.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ContactRequestDto
{
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Name must be between 5-30 characters")
    private String name;

    @NotBlank(message = "Email cannot empty")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}", message = "Phone number must have 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 5, max = 500, message = "Message must have 5-500 characters")
    private String message;
}
