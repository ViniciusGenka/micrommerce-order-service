package com.genka.orderservice.infra.mappers;

import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDTO mapEntityToDTO(Order order) {
        return OrderDTO.mapFromEntity(order);
    }
}
