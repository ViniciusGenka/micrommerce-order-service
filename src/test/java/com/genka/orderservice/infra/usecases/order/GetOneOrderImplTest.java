package com.genka.orderservice.infra.usecases.order;

import com.genka.orderservice.application.exceptions.EntityNotFoundException;
import com.genka.orderservice.application.gateways.OrderDatabaseGateway;
import com.genka.orderservice.domain.entities.order.Order;
import com.genka.orderservice.domain.entities.order.OrderItem;
import com.genka.orderservice.infra.mappers.OrderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOneOrderImplTest {
    @Mock
    private OrderDatabaseGateway orderDatabaseGatewayMock;
    @Mock
    private OrderMapper orderMapperMock;
    @InjectMocks
    private GetOneOrderImpl sut;

    @Test
    @DisplayName("It should call the 'findOrderById' method from OrderDatabaseGateway and then map the Order to a OrderDTO if the order is found")
    void getOrderIfFound() {
        OrderItem item = OrderItem.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(10)
                .unitPrice(BigDecimal.valueOf(10))
                .totalPrice(BigDecimal.valueOf(100))
                .build();
        UUID existingOrderId = UUID.randomUUID();
        Order order = Order.builder()
                .id(existingOrderId)
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(item))
                .buyerEmailAddress("buyer@email.address.com")
                .build();
        when(orderDatabaseGatewayMock.findOrderById(existingOrderId)).thenReturn(Optional.of(order));
        sut.execute(existingOrderId);
        verify(orderDatabaseGatewayMock, times(1)).findOrderById(existingOrderId);
        verify(orderMapperMock, times(1)).mapEntityToDTO(order);
    }

    @Test
    @DisplayName("It should throw an exception if the order is not found")
    void orderNotFound() {
        UUID nonexistentOrderId = UUID.randomUUID();
        when(orderDatabaseGatewayMock.findOrderById(nonexistentOrderId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            sut.execute(nonexistentOrderId);
        });
    }
}