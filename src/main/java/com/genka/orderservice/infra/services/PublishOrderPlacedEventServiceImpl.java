package com.genka.orderservice.infra.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genka.orderservice.application.messaging.MessagePublisher;
import com.genka.orderservice.application.messaging.dtos.OrderItemPlacedEvent;
import com.genka.orderservice.application.messaging.dtos.OrderPlacedEvent;
import com.genka.orderservice.application.services.PublishOrderPlacedEventService;
import com.genka.orderservice.domain.entities.order.Order;
import org.springframework.stereotype.Service;

@Service
public class PublishOrderPlacedEventServiceImpl implements PublishOrderPlacedEventService {

    private final MessagePublisher messagePublisher;
    private final ObjectMapper objectMapper;

    public PublishOrderPlacedEventServiceImpl(MessagePublisher messagePublisher, ObjectMapper objectMapper) {
        this.messagePublisher = messagePublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(Order order) {
        try {
            OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.mapFromEntity(order);
            this.messagePublisher.sendMessage(
                    "order_placed",
                    objectMapper.writeValueAsString(orderPlacedEvent)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
