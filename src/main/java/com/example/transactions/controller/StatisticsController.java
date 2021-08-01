package com.example.transactions.controller;

import com.example.transactions.model.Statistics;
import com.example.transactions.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    @Qualifier("statisticsServiceImpl")
    StatisticsService statisticsService;

    /**
     * Get statistics based on the transactions of last 60 seconds
     *
     * @return
     */
    @GetMapping
    ResponseEntity<Statistics> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

}
