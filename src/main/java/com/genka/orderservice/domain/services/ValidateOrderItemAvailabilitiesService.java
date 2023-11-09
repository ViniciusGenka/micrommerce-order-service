package com.genka.orderservice.domain.services;

import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;

import java.util.List;

public interface ValidateOrderItemAvailabilitiesService {
    void execute(List<StockToCheck> stocksToCheck);
}
