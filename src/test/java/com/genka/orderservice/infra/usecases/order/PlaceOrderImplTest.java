package com.genka.orderservice.infra.usecases.order;

import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.application.services.PublishOrderPlacedEventService;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemInput;
import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;
import com.genka.orderservice.application.usecases.order.dtos.StockToDecrement;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import com.genka.orderservice.domain.services.DecrementInventoryStockService;
import com.genka.orderservice.domain.services.ValidateOrderItemAvailabilitiesService;
import com.genka.orderservice.infra.mappers.OrderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceOrderImplTest {
    @Mock
    private OrderDatabaseGateway orderDatabaseGatewayMock;
    @Mock
    private ValidateOrderItemAvailabilitiesService validateOrderItemAvailabilitiesServiceMock;
    @Mock
    private DecrementInventoryStockService decrementInventoryStockServiceMock;
    @Mock
    private PublishOrderPlacedEventService publishOrderPlacedEventServiceMock;
    @Mock
    private OrderMapper orderMapperMock;
    @InjectMocks
    private PlaceOrderImpl sut;

    @Test
    @DisplayName("It should validate the OrderItems availabilities, decrement the inventory stocks, save the order and publish the OrderPlacedEvent if all OrderItems are available")
    void placeOrderIfAllItemsAreAvailable() {
        String buyerEmailAddress = "buyer@email.address.com";
        OrderItemInput itemInput = OrderItemInput.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .build();
        PlaceOrderInput input = PlaceOrderInput.builder()
                .items(Collections.singletonList(itemInput))
                .buyerEmailAddress(buyerEmailAddress)
                .build();
        OrderItem expectedOrderItem = OrderItem.builder()
                .name(itemInput.getName())
                .productId(itemInput.getProductId())
                .quantity(itemInput.getQuantity())
                .unitPrice(itemInput.getUnitPrice())
                .totalPrice(itemInput.getUnitPrice().multiply(BigDecimal.valueOf(itemInput.getQuantity())))
                .build();
        Order expectedOrder = Order.builder()
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(expectedOrderItem))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        Order savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(expectedOrderItem))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        List<StockToCheck> stocksToCheck = expectedOrder.getItems().stream().map(StockToCheck::new).toList();
        List<StockToDecrement> stocksToDecrement = expectedOrder.getItems().stream().map(StockToDecrement::new).toList();
        when(orderDatabaseGatewayMock.saveOrder(expectedOrder)).thenReturn(savedOrder);
        sut.execute(input);
        verify(validateOrderItemAvailabilitiesServiceMock, times(1)).execute(stocksToCheck);
        verify(decrementInventoryStockServiceMock, times(1)).execute(stocksToDecrement);
        verify(orderDatabaseGatewayMock, times(1)).saveOrder(expectedOrder);
        verify(publishOrderPlacedEventServiceMock, times(1)).execute(savedOrder);
    }
}