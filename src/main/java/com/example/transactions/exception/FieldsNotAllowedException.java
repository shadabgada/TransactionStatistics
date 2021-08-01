package com.example.transactions.exception;

public class FieldsNotAllowedException extends RuntimeException {

    public FieldsNotAllowedException(String message){
        super(message);
    }

}
