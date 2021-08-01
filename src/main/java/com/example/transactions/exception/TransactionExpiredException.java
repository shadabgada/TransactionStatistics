package com.example.transactions.exception;

public class TransactionExpiredException extends RuntimeException {

    public TransactionExpiredException(String message) {
        super(message);
    }
}
