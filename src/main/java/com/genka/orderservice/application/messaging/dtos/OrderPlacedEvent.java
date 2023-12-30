package com.genka.orderservice.application.messaging.dtos;

import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    UUID orderId;
    BigDecimal orderPrice;
    List<OrderItemPlacedEvent> orderItems;
    String buyerEmailAddress;

    public static OrderPlacedEvent mapFromEntity(Order order) {
        return OrderPlacedEvent.builder()
                .orderId(order.getId())
                .orderPrice(order.getPrice())
                .orderItems(order.getItems().stream().map(OrderItemPlacedEvent::mapFromEntity).toList())
                .buyerEmailAddress(order.getBuyerEmailAddress())
                .build();
    }
}
