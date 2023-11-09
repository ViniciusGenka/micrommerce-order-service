package com.genka.orderservice.infra.usecases.order;

import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.application.services.PublishOrderPlacedEventService;
import com.genka.orderservice.application.usecases.order.PlaceOrder;
import com.genka.orderservice.application.usecases.order.dtos.*;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import com.genka.orderservice.domain.services.DecrementInventoryStockService;
import com.genka.orderservice.domain.services.ValidateOrderItemAvailabilitiesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceOrderImpl implements PlaceOrder {
    private final OrderDatabaseGateway orderDatabaseGateway;
    private final DecrementInventoryStockService decrementInventoryStockService;
    private final PublishOrderPlacedEventService publishOrderPlacedEventService;
    private final ValidateOrderItemAvailabilitiesService validateOrderItemAvailabilitiesService;


    public PlaceOrderImpl(OrderDatabaseGateway orderDatabaseGateway, DecrementInventoryStockService decrementInventoryStockService, PublishOrderPlacedEventService publishOrderPlacedEventService, ValidateOrderItemAvailabilitiesService validateOrderItemAvailabilitiesService) {
        this.orderDatabaseGateway = orderDatabaseGateway;
        this.decrementInventoryStockService = decrementInventoryStockService;
        this.publishOrderPlacedEventService = publishOrderPlacedEventService;
        this.validateOrderItemAvailabilitiesService = validateOrderItemAvailabilitiesService;
    }

    @Override
    public void execute(PlaceOrderInput placeOrderInput) {
        List<OrderItem> orderItems = placeOrderInput.getItems().stream().map(OrderItem::new).toList();
        Order order = new Order(orderItems, placeOrderInput.getBuyerEmailAddress());
        List<StockToCheck> stocksToCheck = order.getItems().stream().map(StockToCheck::new).toList();
        this.validateOrderItemAvailabilitiesService.execute(stocksToCheck);
        List<StockToDecrement> stocksToDecrement = order.getItems().stream().map(StockToDecrement::new).toList();
        this.decrementInventoryStockService.execute(stocksToDecrement);
        Order savedOrder = this.orderDatabaseGateway.saveOrder(order);
        this.publishOrderPlacedEventService.execute(savedOrder);
    }
}
