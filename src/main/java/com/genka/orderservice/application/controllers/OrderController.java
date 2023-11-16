package com.genka.orderservice.application.controllers;

import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderController {
    ResponseEntity<OrderDTO> placeOrder(PlaceOrderInput placeOrderInput);

    ResponseEntity<OrderDTO> getOneOrder(UUID orderId);

    ResponseEntity<List<OrderDTO>> getBuyerOrders(String buyerEmailAddress);
}
