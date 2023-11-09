package com.genka.orderservice.infra.usecases.order;

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
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBuyerOrdersImplTest {

    @Mock
    private OrderDatabaseGateway orderDatabaseGatewayMock;
    @Mock
    private OrderMapper orderMapperMock;
    @InjectMocks
    private GetBuyerOrdersImpl sut;

    @Test
    @DisplayName("It should call the 'findOrdersByBuyerEmailAddress' method from OrderDatabaseGateway and map the Orders to OrderDTOs")
    void getMappedBuyerOrders() {
        String existingBuyerEmailAddress = "buyer@email.address.com";
        OrderItem item = OrderItem.builder()
                .name("item name")
                .productId(UUID.randomUUID())
                .quantity(10)
                .unitPrice(BigDecimal.valueOf(10))
                .totalPrice(BigDecimal.valueOf(100))
                .build();
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .price(BigDecimal.valueOf(100))
                .items(Collections.singletonList(item))
                .buyerEmailAddress(existingBuyerEmailAddress)
                .build();
        when(orderDatabaseGatewayMock.findOrdersByBuyerEmailAddress(existingBuyerEmailAddress)).thenReturn(Collections.singletonList(order));
        sut.execute(existingBuyerEmailAddress);
        verify(orderDatabaseGatewayMock, times(1)).findOrdersByBuyerEmailAddress(existingBuyerEmailAddress);
        verify(orderMapperMock, times(1)).mapEntityToDTO(order);
    }
}