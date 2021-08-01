package com.example.transactions.exception;

public class NoTransactionsFoundException extends RuntimeException {

    public NoTransactionsFoundException(String message) {
        super(message);
    }

}
