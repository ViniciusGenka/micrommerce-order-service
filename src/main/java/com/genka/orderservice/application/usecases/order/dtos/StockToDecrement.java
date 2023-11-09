package com.genka.orderservice.application.usecases.order.dtos;

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
public class StockToDecrement {
    UUID productId;
    Integer quantityToDecrement;

    public StockToDecrement(OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.quantityToDecrement = orderItem.getQuantity();
    }
}
