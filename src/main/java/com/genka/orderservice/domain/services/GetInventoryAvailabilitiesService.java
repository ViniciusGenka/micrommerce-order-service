package com.genka.orderservice.domain.services;

import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;

import java.util.List;

public interface GetInventoryAvailabilitiesService {
    List<OrderItemAvailability> execute(List<StockToCheck> stocksToCheck);
}
