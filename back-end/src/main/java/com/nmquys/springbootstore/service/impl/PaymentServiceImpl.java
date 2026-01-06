package com.nmquys.springbootstore.service.impl;

import com.nmquys.springbootstore.dto.PaymentIntentRequestDto;
import com.nmquys.springbootstore.dto.PaymentIntentResponseDto;
import com.nmquys.springbootstore.service.IPaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements IPaymentService
{
    @Override
    public PaymentIntentResponseDto createPaymentIntent(PaymentIntentRequestDto requestDto)
    {
        try
        {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(requestDto.amount())
                    .setCurrency(requestDto.currency())
                    .addPaymentMethodType("card").build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return new PaymentIntentResponseDto(paymentIntent.getClientSecret());
        }
        catch (StripeException e)
        {
            throw new RuntimeException("Failed to create payment intent", e);
        }

    }
}
