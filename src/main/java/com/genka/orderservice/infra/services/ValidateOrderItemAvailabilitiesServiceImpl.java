package com.genka.orderservice.infra.services;

import com.genka.orderservice.application.exceptions.InternalServerErrorException;
import com.genka.orderservice.application.exceptions.UnavailableInventoryException;
import com.genka.orderservice.application.usecases.order.dtos.OrderItemAvailability;
import com.genka.orderservice.application.usecases.order.dtos.StockToCheck;
import com.genka.orderservice.domain.services.GetInventoryAvailabilitiesService;
import com.genka.orderservice.domain.services.ValidateOrderItemAvailabilitiesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValidateOrderItemAvailabilitiesServiceImpl implements ValidateOrderItemAvailabilitiesService {
    private final GetInventoryAvailabilitiesService getInventoryAvailabilitiesService;

    public ValidateOrderItemAvailabilitiesServiceImpl(GetInventoryAvailabilitiesService getInventoryAvailabilitiesService) {
        this.getInventoryAvailabilitiesService = getInventoryAvailabilitiesService;
    }

    @Override
    public void execute(List<StockToCheck> stocksToCheck) {
        List<OrderItemAvailability> orderItemAvailabilities = this.getInventoryAvailabilitiesService.execute(stocksToCheck);
        if (this.someOrderItemIsUnavailable(orderItemAvailabilities)) {
            List<UUID> outOfStockProductIds = orderItemAvailabilities.stream().filter(item -> !item.getAvailability())
                    .map(OrderItemAvailability::getProductId)
                    .toList();
            if (!outOfStockProductIds.isEmpty()) {
                throw new UnavailableInventoryException("Insufficient stock for products: " + outOfStockProductIds);
            } else {
                throw new InternalServerErrorException("Internal Server Error");
            }
        }
    }

    public Boolean someOrderItemIsUnavailable(List<OrderItemAvailability> orderItemAvailabilities) {
        return !orderItemAvailabilities.stream().allMatch(OrderItemAvailability::getAvailability);
    }
}
