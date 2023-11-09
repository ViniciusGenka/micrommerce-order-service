package com.genka.orderservice.infra.web.http.clients;

import com.genka.orderservice.application.usecases.order.dtos.DecrementInventoryStocksRequest;
import com.genka.orderservice.application.usecases.order.dtos.GetInventoryAvailabilitiesRequest;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @PostMapping(value = "/inventories/availabilities", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderItemAvailability>> getOrderItemAvailabilities(@RequestBody GetInventoryAvailabilitiesRequest getInventoryAvailabilitiesRequest);

    @PostMapping(value = "/inventories/stocks/decrement", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> decrementInventoryStocks(@RequestBody DecrementInventoryStocksRequest decrementInventoryStocksRequest);
}


