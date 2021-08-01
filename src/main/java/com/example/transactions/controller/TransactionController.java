package com.example.transactions.controller;

import com.example.transactions.dto.Transaction;
import com.example.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/transactions")
public class TransactionController {


    @Autowired
    @Qualifier("transactionServiceImpl")
    TransactionService transactionService;

    /**
     * Add Transaction
     * @param transaction
     */
    @PostMapping
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        transactionService.addTransaction(transaction);
        return ResponseEntity.created(URI.create("/transactions")).build();
    }

    /**
     * Delete all Transactions
     * @return
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllTransactions() {
        transactionService.deleteTransactions();
        return ResponseEntity.noContent().build();
    }

}
