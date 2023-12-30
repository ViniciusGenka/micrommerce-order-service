package com.genka.orderservice.domain.entities.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal price;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> items = new ArrayList<>();
    private String buyerEmailAddress;

    public Order(List<OrderItem> items, String buyerEmailAddress) {
        this.items = items;
        this.buyerEmailAddress = buyerEmailAddress;
        this.price = calculateOrderPrice();
    }

    private BigDecimal calculateOrderPrice() {
        return this.items
                .stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
