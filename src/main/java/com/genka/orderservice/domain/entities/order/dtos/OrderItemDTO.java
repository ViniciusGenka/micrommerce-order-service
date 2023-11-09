package com.genka.orderservice.domain.entities.order.dtos;

import com.genka.orderservice.domain.entities.order.OrderItem;
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
public class OrderItemDTO {
    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private Integer quantity;

    public static OrderItemDTO mapFromEntity(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .price(orderItem.getTotalPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
