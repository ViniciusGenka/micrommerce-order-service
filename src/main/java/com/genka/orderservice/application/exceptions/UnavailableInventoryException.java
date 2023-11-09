package com.genka.orderservice.application.exceptions;

public class UnavailableInventoryException extends RuntimeException {
    public UnavailableInventoryException(String msg) {
        super(msg);
    }
}
