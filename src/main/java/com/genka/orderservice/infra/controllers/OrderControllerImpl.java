package com.genka.orderservice.infra.controllers;

import com.genka.orderservice.application.controllers.OrderController;
import com.genka.orderservice.application.usecases.order.GetBuyerOrders;
import com.genka.orderservice.application.usecases.order.GetOneOrder;
import com.genka.orderservice.application.usecases.order.PlaceOrder;
import com.genka.orderservice.application.usecases.order.dtos.PlaceOrderInput;
import com.genka.orderservice.domain.entities.order.dtos.OrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final PlaceOrder placeOrderUseCase;
    private final GetOneOrder getOneOrderUseCase;
    private final GetBuyerOrders getBuyerOrdersUseCase;

    public OrderControllerImpl(PlaceOrder placeOrderUseCase, GetOneOrder getOneOrderUseCase, GetBuyerOrders getBuyerOrdersUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.getOneOrderUseCase = getOneOrderUseCase;
        this.getBuyerOrdersUseCase = getBuyerOrdersUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody PlaceOrderInput placeOrderInput) {
        this.placeOrderUseCase.execute(placeOrderInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOneOrder(@PathVariable UUID id) {
        OrderDTO order = this.getOneOrderUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @Override
    @GetMapping("/buyers/{email}")
    public ResponseEntity<List<OrderDTO>> getBuyerOrders(@PathVariable String email) {
        List<OrderDTO> orders = this.getBuyerOrdersUseCase.execute(email);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
