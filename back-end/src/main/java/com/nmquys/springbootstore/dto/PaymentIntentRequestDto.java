package com.nmquys.springbootstore.dto;

public record PaymentIntentRequestDto(Long amount, String currency) {
}
