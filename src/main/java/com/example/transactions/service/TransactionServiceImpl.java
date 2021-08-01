package com.example.transactions.service;

import com.example.transactions.common.TransactionConstants;
import com.example.transactions.dto.Transaction;
import com.example.transactions.exception.FieldsNotAllowedException;
import com.example.transactions.exception.TransactionExpiredException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final List<Transaction> transactions;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();

    @Override
    public void addTransaction(Transaction transaction) {
        log.info("BEGIN: Transaction Service: addTransaction {} ", transaction);

        try {
            writeLock.lock();

            OffsetDateTime inputOffsetDateTime = OffsetDateTime.parse(transaction.getTimestamp());

            if (OffsetDateTime.now(ZoneOffset.UTC).compareTo(inputOffsetDateTime) < 0) {
                throw new FieldsNotAllowedException(TransactionConstants.FUTURE_DATE_NOT_ALLOWED);
            }

            if (inputOffsetDateTime.compareTo(OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(TransactionConstants.EXPIRY_SECONDS)) < 0) {
                throw new TransactionExpiredException(TransactionConstants.TRANSACTION_EXPIRED);
            }

            transactions.add(transaction);

        } finally {
            writeLock.unlock();
        }

        log.info("END: Transaction Service: addTransaction");
    }

    @Override
    public List<Transaction> getAllTransactions() {
        log.info("BEGIN: Transaction Service: getAllTransactions");

        try {
            writeLock.lock();

            transactions.removeIf(this::isTransactionExpired);

        } finally {
            writeLock.unlock();
        }

        log.info("END: Transaction Service: getAllTransactions");
        return transactions;
    }

    private boolean isTransactionExpired(Transaction transaction) {
        OffsetDateTime inputOffsetDateTime = OffsetDateTime.parse(transaction.getTimestamp());
        OffsetDateTime thresholdOffsetDateTime =
                OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(TransactionConstants.EXPIRY_SECONDS);
        return inputOffsetDateTime.compareTo(thresholdOffsetDateTime) < 0;
    }

    @Override
    public void deleteTransactions() {
        log.info("BEGIN: Transaction Service: deleteTransactions");

        try {
            writeLock.lock();

            transactions.clear();

        } finally {
            writeLock.unlock();
        }

        log.info("END: Transaction Service: deleteTransactions");
    }
}
