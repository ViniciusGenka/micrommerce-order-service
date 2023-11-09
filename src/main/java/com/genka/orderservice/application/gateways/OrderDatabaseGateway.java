package com.genka.orderservice.application.gateways;

import com.genka.orderservice.domain.entities.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderDatabaseGateway {
    Order saveOrder(Order order);

    Optional<Order> findOrderById(UUID orderId);

    List<Order> findOrdersByBuyerEmailAddress(String buyerEmailAddress);
}
