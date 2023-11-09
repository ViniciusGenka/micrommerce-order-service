package com.genka.orderservice.application.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnavailableInventoryException.class)
    public ResponseEntity<ErrorDetails> unavailableInventoryException(UnavailableInventoryException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorDetails> internalServerErrorException(InternalServerErrorException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}