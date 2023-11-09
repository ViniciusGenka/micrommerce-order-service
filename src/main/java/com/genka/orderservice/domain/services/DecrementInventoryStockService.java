package com.genka.orderservice.domain.services;

import com.genka.orderservice.application.usecases.order.dtos.StockToDecrement;

import java.util.List;

public interface DecrementInventoryStockService {
    void execute(List<StockToDecrement> stocksToDecrement);
}
