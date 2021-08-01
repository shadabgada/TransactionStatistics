package com.example.transactions.service;

import com.example.transactions.dto.Transaction;
import com.example.transactions.exception.NoTransactionsFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.example.transactions.common.TransactionConstants.EXPIRY_SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatisticsServiceTests {

    @Mock
    StatisticsServiceImpl statisticsService;

    @Mock
    TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        List<Transaction> transactions = new ArrayList<>();
        transactionService = new TransactionServiceImpl(transactions);
        statisticsService = new StatisticsServiceImpl(transactionService);
    }

    @Test()
    public void test_Get_Statistics_Success() {

        Transaction transaction = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        transactionService.addTransaction(transaction);
        statisticsService.getStatistics();
    }

    @Test()
    public void test_Get_Statistics_For_Expired_Transactions() throws InterruptedException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        transactionService.addTransaction(transaction1);

        Transaction transaction2 = new Transaction(new BigDecimal("1230.34"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        transactionService.addTransaction(transaction2);

        //Adding sleep time so that previous transactions gets expired
        Thread.sleep(EXPIRY_SECONDS*1000);

        Transaction transaction3 = new Transaction(new BigDecimal("2050.35"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        transactionService.addTransaction(transaction3);

        Transaction transaction4 = new Transaction(new BigDecimal("1050.35"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        transactionService.addTransaction(transaction4);

        assert statisticsService.getStatistics().getCount() == 2;
        assert statisticsService.getStatistics().getMax().equals(new BigDecimal("2050.35"));
        assert statisticsService.getStatistics().getMin().equals(new BigDecimal("1050.35"));
        assert statisticsService.getStatistics().getAvg().equals(new BigDecimal("1550.35"));
        assert statisticsService.getStatistics().getSum().equals(new BigDecimal("3100.70"));

    }

    @Test
    public void test_Concurrent_Access() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            transactionService.addTransaction(new Transaction(new BigDecimal("100"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC))));
        });

        Thread t2 = new Thread(() -> {
            transactionService.addTransaction(new Transaction(new BigDecimal("200"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC))));
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @Test()
    public void test_Get_Statistics_Has_No_Transactions() {

        //remove all transactions
        transactionService.deleteTransactions();

        Exception actualException = assertThrows(NoTransactionsFoundException.class,
                () -> statisticsService.getStatistics());

        String expected = "No transactions found in last " + EXPIRY_SECONDS + " seconds";
        assertEquals(expected, actualException.getMessage());

    }
}
