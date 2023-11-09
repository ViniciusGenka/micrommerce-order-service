package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.usecases.order.dtos.DecrementInventoryStocksRequest;
import com.genka.orderservice.application.usecases.order.dtos.StockToDecrement;
import com.genka.orderservice.infra.web.http.clients.InventoryClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DecrementInventoryStockServiceImplTest {

    @Mock
    private InventoryClient inventoryClientMock;
    @InjectMocks
    private DecrementInventoryStockServiceImpl sut;

    @Test
    @DisplayName("It should send a request via InventoryClient to decrement the inventory stocks")
    void sendRequestToDecrementInventoryStocks() {
        StockToDecrement stockToDecrement = StockToDecrement.builder()
                .productId(UUID.randomUUID())
                .quantityToDecrement(1)
                .build();
        DecrementInventoryStocksRequest decrementInventoryStocksRequestBody = new DecrementInventoryStocksRequest(Collections.singletonList(stockToDecrement));
        sut.execute(Collections.singletonList(stockToDecrement));
        verify(inventoryClientMock, times(1)).decrementInventoryStocks(decrementInventoryStocksRequestBody);
    }

}