package com.genka.orderservice.application.messaging.dtos;

import com.genka.orderservice.domain.entities.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemPlacedEvent {
    UUID productId;
    Integer quantity;
    String name;

    public OrderItemPlacedEvent(OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.quantity = orderItem.getQuantity();
        this.name = orderItem.getName();
    }
}
