package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.exceptions.UnavailableInventoryException;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;
import com.genka.orderservice.domain.services.GetInventoryAvailabilitiesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateOrderItemAvailabilitiesServiceImplTest {

    @Mock
    private GetInventoryAvailabilitiesService getInventoryAvailabilitiesServiceMock;
    @InjectMocks
    @Spy
    private ValidateOrderItemAvailabilitiesServiceImpl sut;

    @Test
    @DisplayName("It should not throw an exception if all OrderItems are available")
    void allOrderItemsAreAvailable() {
        StockToCheck stockToCheck = StockToCheck.builder()
                .productId(UUID.randomUUID())
                .quantityToCheck(1)
                .build();
        OrderItemAvailability orderItemAvailability = OrderItemAvailability.builder()
                .productId(stockToCheck.getProductId())
                .availability(Boolean.TRUE)
                .build();
        when(getInventoryAvailabilitiesServiceMock.execute(Collections.singletonList(stockToCheck))).thenReturn(Collections.singletonList(orderItemAvailability));
        when(sut.someOrderItemIsUnavailable(Collections.singletonList(orderItemAvailability))).thenReturn(Boolean.FALSE);
        assertDoesNotThrow(() -> sut.execute(Collections.singletonList(stockToCheck)));
    }

    @Test
    @DisplayName("It should throw an exception if some OrderItem is unavailable")
    void someOrderItemIsUnavailable() {
        StockToCheck stockToCheck = StockToCheck.builder()
                .productId(UUID.randomUUID())
                .quantityToCheck(1)
                .build();
        OrderItemAvailability orderItemAvailability = OrderItemAvailability.builder()
                .productId(stockToCheck.getProductId())
                .availability(Boolean.FALSE)
                .build();
        when(getInventoryAvailabilitiesServiceMock.execute(Collections.singletonList(stockToCheck))).thenReturn(Collections.singletonList(orderItemAvailability));
        when(sut.someOrderItemIsUnavailable(Collections.singletonList(orderItemAvailability))).thenReturn(Boolean.TRUE);
        assertThrows(UnavailableInventoryException.class, () -> sut.execute(Collections.singletonList(stockToCheck)));
    }

}