package com.genka.orderservice.application.usecases.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemInput {
    private String name;
    private UUID productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}