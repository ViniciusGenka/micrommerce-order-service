package com.genka.orderservice.infra.repositories.order.mysql;

import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.domain.entities.order.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderDatabaseGatewayMysql implements OrderDatabaseGateway {

    private final OrderRepositoryMysql orderRepository;

    public OrderDatabaseGatewayMysql(OrderRepositoryMysql orderRepositoryMysql) {
        this.orderRepository = orderRepositoryMysql;
    }

    @Override
    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public Optional<Order> findOrderById(UUID orderId) {
        return this.orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findOrdersByBuyerEmailAddress(String buyerEmailAddress) {
        return this.orderRepository.findByBuyerEmailAddress(buyerEmailAddress);
    }
}
