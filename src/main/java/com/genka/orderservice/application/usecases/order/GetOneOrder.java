package com.genka.orderservice.application.usecases.order;

import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;

import java.util.UUID;

public interface GetOneOrder {
    OrderDTO execute(UUID orderId);
}
