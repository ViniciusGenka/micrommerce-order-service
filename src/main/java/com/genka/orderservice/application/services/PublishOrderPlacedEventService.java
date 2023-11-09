package com.genka.orderservice.application.services;


import com.genka.orderservice.domain.entities.order.Order;

public interface PublishOrderPlacedEventService {
    void execute(Order order);
}
