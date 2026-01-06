package com.nmquys.springbootstore.service;

import com.nmquys.springbootstore.dto.PaymentIntentRequestDto;
import com.nmquys.springbootstore.dto.PaymentIntentResponseDto;

public interface IPaymentService
{

    PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto requestDto);
}
