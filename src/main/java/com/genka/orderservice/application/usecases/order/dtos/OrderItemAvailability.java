package com.genka.orderservice.application.usecases.order.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemAvailability {
    UUID productId;
    Boolean availability;
}
