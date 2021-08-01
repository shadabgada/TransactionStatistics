package com.example.transactions.service;

import com.example.transactions.common.TransactionConstants;
import com.example.transactions.dto.Transaction;
import com.example.transactions.exception.NoTransactionsFoundException;
import com.example.transactions.model.Statistics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    @Qualifier("transactionServiceImpl")
    TransactionService transactionService;

    @Override
    public Statistics getStatistics() {
        log.info("BEGIN: Statistics Service: getStatistics");

        List<Transaction> transactions = transactionService.getAllTransactions();

        if (CollectionUtils.isEmpty(transactions)) {
            throw new NoTransactionsFoundException(TransactionConstants.NO_TRANSACTIONS_FOUND);
        }

        Statistics statistics = new Statistics();

        BigDecimal sum = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.setSum(sum.setScale(2, RoundingMode.HALF_UP));

        statistics.setAvg(sum.divide(BigDecimal.valueOf(transactions.size())).setScale(2, RoundingMode.HALF_UP));

        BigDecimal max = transactions.stream()
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(null);
        statistics.setMax(max.setScale(2, RoundingMode.HALF_UP));

        BigDecimal min = transactions.stream()
                .map(Transaction::getAmount)
                .min(BigDecimal::compareTo)
                .orElse(null);
        statistics.setMin(min.setScale(2, RoundingMode.HALF_UP));

        statistics.setCount((long) transactions.size());

        log.info("END: Statistics Service: getStatistics");
        return statistics;
    }

}
