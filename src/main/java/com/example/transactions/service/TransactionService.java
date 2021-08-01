package com.example.transactions.service;

import com.example.transactions.dto.Transaction;

import java.util.List;

public interface TransactionService {

    void addTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();

    void deleteTransactions();

}
