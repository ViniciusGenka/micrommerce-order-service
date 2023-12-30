package com.genka.orderservice.infra.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genka.orderservice.application.messaging.MessagePublisher;
import com.genka.orderservice.application.messaging.dtos.OrderItemPlacedEvent;
import com.genka.orderservice.application.messaging.dtos.OrderPlacedEvent;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishOrderPlacedEventServiceImplTest {

    @Mock
    private MessagePublisher messagePublisherMock;
    @Spy
    private ObjectMapper objectMapperMock;
    @InjectMocks
    private PublishOrderPlacedEventServiceImpl sut;

    @Test
    @DisplayName("It should send the OrderPlacedEvent through a message publisher")
    void publishEvent() throws JsonProcessingException {
        OrderItem item = OrderItem.builder()
                .name("any name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .totalPrice(BigDecimal.valueOf(100))
                .build();
        Order placedOrder = Order.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(item))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        OrderPlacedEvent expectedOrderPlacedEvent = OrderPlacedEvent.builder()
                .orderId(placedOrder.getId())
                .orderPrice(placedOrder.getPrice())
                .orderItems(placedOrder.getItems().stream().map(OrderItemPlacedEvent::mapFromEntity).toList())
                .buyerEmailAddress(placedOrder.getBuyerEmailAddress())
                .build();
        sut.execute(placedOrder);
        verify(objectMapperMock, times(1)).writeValueAsString(expectedOrderPlacedEvent);
        verify(messagePublisherMock, times(1)).sendMessage(
                "order_placed",
                objectMapperMock.writeValueAsString(expectedOrderPlacedEvent)
        );
    }

    @Test
    @DisplayName("It should handle the JsonProcessingException if it is thrown")
    void handleException() throws JsonProcessingException {
        OrderItem item = OrderItem.builder()
                .name("any name")
                .productId(UUID.randomUUID())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .totalPrice(BigDecimal.valueOf(100))
                .build();
        Order placedOrder = Order.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(item))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        OrderPlacedEvent expectedOrderPlacedEvent = OrderPlacedEvent.builder()
                .orderId(placedOrder.getId())
                .orderPrice(placedOrder.getPrice())
                .orderItems(placedOrder.getItems().stream().map(OrderItemPlacedEvent::mapFromEntity).toList())
                .buyerEmailAddress(placedOrder.getBuyerEmailAddress())
                .build();
        when(objectMapperMock.writeValueAsString(expectedOrderPlacedEvent)).thenThrow(JsonProcessingException.class);
        assertThrows(RuntimeException.class, () -> {
            sut.execute(placedOrder);
        });
    }

}