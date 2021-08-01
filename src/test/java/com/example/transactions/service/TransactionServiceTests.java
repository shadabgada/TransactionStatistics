package com.example.transactions.service;


import com.example.transactions.dto.Transaction;
import com.example.transactions.exception.FieldsNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionServiceTests {

    @Mock
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        List<Transaction> transactions = new ArrayList<>();
        transactionService = new TransactionServiceImpl(transactions);
    }

    @Test()
    public void test_AddTransaction_Success() {

        Transaction transaction = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));

        transactionService.addTransaction(transaction);

        assert transactionService.getAllTransactions().size() > 0;
    }

    @Test()
    public void test_AddTransaction_Has_Invalid_Timestamp() {

        Transaction transaction = new Transaction(new BigDecimal("2130.345"), "2021-07-3115:46:00.431563200Z");

        Exception actualException = assertThrows(DateTimeParseException.class,
                () -> transactionService.addTransaction(transaction));

        String expected = "Text '2021-07-3115:46:00.431563200Z' could not be parsed at index 10";
        assertEquals(expected, actualException.getMessage());

    }

    @Test()
    public void test_AddTransaction_Has_Future_Timestamp() {

        Transaction transaction = new Transaction(new BigDecimal("2130.345"), "9999-07-31T15:46:00.431563200Z");

        Exception actualException = assertThrows(FieldsNotAllowedException.class,
                () -> transactionService.addTransaction(transaction));

        String expected = "Future date is not allowed";
        assertEquals(expected, actualException.getMessage());

    }

    @Test()
    public void test_DeleteTransactions() {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));

        transactionService.addTransaction(transaction1);

        transactionService.deleteTransactions();

        assert transactionService.getAllTransactions().size()==0;

    }

}
