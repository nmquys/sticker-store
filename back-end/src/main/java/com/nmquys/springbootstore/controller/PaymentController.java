package com.nmquys.springbootstore.controller;

import com.nmquys.springbootstore.dto.PaymentIntentRequestDto;
import com.nmquys.springbootstore.dto.PaymentIntentResponseDto;
import com.nmquys.springbootstore.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService iPaymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponseDto> createPaymentIntent(@RequestBody PaymentIntentRequestDto paymentRequest)
    {
        PaymentIntentResponseDto response = iPaymentService.createPaymentIntent(paymentRequest);
        return ResponseEntity.ok(response);
    }

}
