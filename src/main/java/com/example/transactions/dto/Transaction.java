package com.example.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transaction {

    private BigDecimal amount;

    private String timestamp;
}
