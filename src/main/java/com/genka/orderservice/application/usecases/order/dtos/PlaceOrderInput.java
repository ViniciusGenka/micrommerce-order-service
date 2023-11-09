package com.genka.orderservice.application.usecases.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderInput {
    private List<OrderItemInput> items;
    private String buyerEmailAddress;
}
