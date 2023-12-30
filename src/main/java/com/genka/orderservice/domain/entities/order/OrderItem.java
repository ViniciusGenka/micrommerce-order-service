package com.genka.orderservice.domain.entities.order;

import com.genka.orderservice.application.usecases.order.dtos.OrderItemInput;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID productId;
    private Integer quantity;
    private String name;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public OrderItem(String name, UUID productId, Integer quantity, BigDecimal unitPrice) {
        this.name = name;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = calculateOrderItemTotalPrice(unitPrice);
    }

    public OrderItem(OrderItemInput orderItemInput) {
        this.name = orderItemInput.getName();
        this.productId = orderItemInput.getProductId();
        this.quantity = orderItemInput.getQuantity();
        this.unitPrice = orderItemInput.getUnitPrice();
        this.totalPrice = calculateOrderItemTotalPrice(orderItemInput.getUnitPrice());
    }

    private BigDecimal calculateOrderItemTotalPrice(BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}
