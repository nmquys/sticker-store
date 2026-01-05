package com.nmquys.springbootstore.service;

import com.nmquys.springbootstore.dto.OrderRequestDto;
import com.nmquys.springbootstore.dto.OrderResponseDto;
import com.nmquys.springbootstore.entity.Order;

import java.util.List;

public interface IOrderService {

    void createOrder(OrderRequestDto orderRequest);

    List<OrderResponseDto> getCustomerOrders();

    List<OrderResponseDto> getAllPendingOrders();

    void updateOrderStatus(Long orderId, String orderStatus);
}
