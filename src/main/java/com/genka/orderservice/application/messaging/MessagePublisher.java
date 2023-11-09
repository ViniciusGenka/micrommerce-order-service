package com.genka.orderservice.application.messaging;

public interface MessagePublisher {
    void sendMessage(String channel, String message);
}
