package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.usecases.order.dtos.GetInventoryAvailabilitiesRequest;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;
import com.genka.orderservice.infra.web.http.clients.InventoryClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetInventoryAvailabilitiesServiceImplTest {
    @Mock
    private InventoryClient inventoryClientMock;
    @InjectMocks
    private GetInventoryAvailabilitiesServiceImpl sut;

    @Test
    @DisplayName("It should send a request via InventoryClient to get the OrderItemAvailabilities and return the body of the ResponseEntity")
    void sendRequestToGetInventoryAvailabilities() {
        StockToCheck stockToCheck = StockToCheck.builder()
                .productId(UUID.randomUUID())
                .quantityToCheck(1)
                .build();
        GetInventoryAvailabilitiesRequest getInventoryAvailabilitiesRequest = new GetInventoryAvailabilitiesRequest(Collections.singletonList(stockToCheck));
        OrderItemAvailability expectedAvailability = OrderItemAvailability.builder()
                .productId(stockToCheck.getProductId())
                .availability(Boolean.TRUE)
                .build();
        ResponseEntity<List<OrderItemAvailability>> response = ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(expectedAvailability));
        when(inventoryClientMock.getOrderItemAvailabilities(getInventoryAvailabilitiesRequest)).thenReturn(response);
        List<OrderItemAvailability> sutResponse = sut.execute(Collections.singletonList(stockToCheck));
        verify(inventoryClientMock, times(1)).getOrderItemAvailabilities(getInventoryAvailabilitiesRequest);
        assertEquals(response.getBody(), sutResponse);
    }
}