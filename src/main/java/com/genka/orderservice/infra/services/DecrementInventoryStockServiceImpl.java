package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.usecases.order.dtos.DecrementInventoryStocksRequest;
import com.genka.orderservice.application.usecases.order.dtos.StockToDecrement;
import com.genka.orderservice.domain.services.DecrementInventoryStockService;
import com.genka.orderservice.infra.web.http.clients.InventoryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecrementInventoryStockServiceImpl implements DecrementInventoryStockService {

    private final InventoryClient inventoryClient;

    public DecrementInventoryStockServiceImpl(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Override
    public void execute(List<StockToDecrement> stocksToDecrement) {
        DecrementInventoryStocksRequest decrementInventoryStocksRequestBody = new DecrementInventoryStocksRequest(stocksToDecrement);
        this.inventoryClient.decrementInventoryStocks(decrementInventoryStocksRequestBody);
    }
}
