package com.genka.orderservice.application.messaging.dtos;

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
}
