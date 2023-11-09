package com.genka.orderservice.application.usecases.order;

import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;

public interface PlaceOrder {
    void execute(PlaceOrderInput placeOrderInput);
}
