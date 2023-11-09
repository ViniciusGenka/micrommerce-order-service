package com.genka.orderservice.application.exceptions;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}