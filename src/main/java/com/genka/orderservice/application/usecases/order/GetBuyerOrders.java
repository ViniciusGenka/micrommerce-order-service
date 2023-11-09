package com.genka.orderservice.application.usecases.order;

import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;

import java.util.List;

public interface GetBuyerOrders {
    List<OrderDTO> execute(String buyerEmailAddress);
}
