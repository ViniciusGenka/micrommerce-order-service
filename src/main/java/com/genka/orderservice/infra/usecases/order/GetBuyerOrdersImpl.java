package com.genka.orderservice.infra.usecases.order;

import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.application.usecases.order.GetBuyerOrders;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import com.genka.orderservice.infra.mappers.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBuyerOrdersImpl implements GetBuyerOrders {
    private final OrderDatabaseGateway orderDatabaseGateway;
    private final OrderMapper orderMapper;

    public GetBuyerOrdersImpl(OrderDatabaseGateway orderDatabaseGateway, OrderMapper orderMapper) {
        this.orderDatabaseGateway = orderDatabaseGateway;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDTO> execute(String buyerEmailAddress) {
        return this.orderDatabaseGateway.findOrdersByBuyerEmailAddress(buyerEmailAddress).stream().map(orderMapper::mapEntityToDTO).toList();
    }
}
