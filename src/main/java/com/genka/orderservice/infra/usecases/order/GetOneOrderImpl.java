package com.genka.orderservice.infra.usecases.order;

import com.genka.orderservice.application.exceptions.EntityNotFoundException;
import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.application.usecases.order.GetOneOrder;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import com.genka.orderservice.infra.mappers.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOneOrderImpl implements GetOneOrder {

    private final OrderDatabaseGateway orderDatabaseGateway;
    private final OrderMapper orderMapper;

    public GetOneOrderImpl(OrderDatabaseGateway orderDatabaseGateway, OrderMapper orderMapper) {
        this.orderDatabaseGateway = orderDatabaseGateway;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO execute(UUID orderId) {
        Order order = this.orderDatabaseGateway.findOrderById(orderId).orElseThrow(() -> new EntityNotFoundException("Order with id" + orderId + " not found"));
        return this.orderMapper.mapEntityToDTO(order);
    }
}
