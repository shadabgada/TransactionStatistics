package com.example.transactions.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = FieldsNotAllowedException.class)
    public String handleFieldsNotAllowedException(FieldsNotAllowedException fieldsNotAllowedException) {
        log.warn("FieldsNotAllowedException Found ", fieldsNotAllowedException);
        return fieldsNotAllowedException.getMessage();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = DateTimeParseException.class)
    public String handleDateTimeParseException(DateTimeParseException dateTimeParseException) {
        log.warn("DateTimeParseException Found ", dateTimeParseException);
        return dateTimeParseException.getMessage();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = NoTransactionsFoundException.class)
    public String handleNoTransactionsFoundException(NoTransactionsFoundException noTransactionsFoundException) {
        log.warn("NoTransactionsFoundException Found ", noTransactionsFoundException);
        return noTransactionsFoundException.getMessage();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = TransactionExpiredException.class)
    public String handleTransactionExpiredException(TransactionExpiredException transactionExpiredException) {
        log.warn("TransactionExpiredException Found ", transactionExpiredException);
        return transactionExpiredException.getMessage();
    }

}
