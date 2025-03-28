package com.order_manager.exception;

public class ProductExistException extends RuntimeException {
    public ProductExistException(String message) {
        super(message);
    }
}
