package com.fetch.rewards.exception;

public class ReceiptNotFoundException extends RuntimeException {
    public ReceiptNotFoundException(String message) {
        super(message);
    }
}