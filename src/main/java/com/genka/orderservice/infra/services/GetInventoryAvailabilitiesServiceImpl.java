package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.usecases.order.dtos.GetInventoryAvailabilitiesRequest;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;
import com.genka.orderservice.infra.web.http.clients.InventoryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetInventoryAvailabilitiesServiceImpl implements com.genka.orderservice.domain.services.GetInventoryAvailabilitiesService {

    private final InventoryClient inventoryClient;

    public GetInventoryAvailabilitiesServiceImpl(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Override
    public List<OrderItemAvailability> execute(List<StockToCheck> stocksToCheck) {
        GetInventoryAvailabilitiesRequest getInventoryAvailabilitiesRequest = new GetInventoryAvailabilitiesRequest(stocksToCheck);
        return this.inventoryClient.getOrderItemAvailabilities(getInventoryAvailabilitiesRequest).getBody();
    }
}
