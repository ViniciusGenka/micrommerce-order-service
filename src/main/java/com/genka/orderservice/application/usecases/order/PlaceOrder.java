package com.genka.orderservice.application.usecases.order;

import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;

public interface PlaceOrder {
    OrderDTO execute(PlaceOrderInput placeOrderInput);
}
