package com.genka.orderservice.application.usecases.order.dtos;

import com.genka.orderservice.domain.entities.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@ToString
public class StockToCheck {
    UUID productId;
    Integer quantityToCheck;

    public StockToCheck(OrderItem item) {
        this.productId = item.getProductId();
        this.quantityToCheck = item.getQuantity();
    }
}
