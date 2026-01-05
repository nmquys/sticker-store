package com.nmquys.springbootstore.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
