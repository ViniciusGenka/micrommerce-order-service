package com.genka.orderservice.domain.entities.order.dtos;

import com.genka.orderservice.domain.entities.order.Order;
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
public class OrderDTO {

    private UUID id;
    private BigDecimal price;
    private List<OrderItemDTO> items;

    public static OrderDTO mapFromEntity(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .price(order.getPrice())
                .items(order.getItems()
                        .stream()
                        .map(OrderItemDTO::mapFromEntity)
                        .toList())
                .build();
    }
}