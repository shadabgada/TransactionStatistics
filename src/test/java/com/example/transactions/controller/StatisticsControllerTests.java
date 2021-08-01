package com.example.transactions.controller;

import com.example.transactions.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsControllerTests {

    private TestRestTemplate testRestTemplate;

    @Value("${server.port}")
    int port;

    String baseURL;

    @BeforeEach
    public void setUp() {

        testRestTemplate = new TestRestTemplate();

        //If dev/prod URL is not supplied, use localhost
        if (StringUtils.isBlank(baseURL)) {
            baseURL = "http://localhost:";
        }

        if (port == 0) {
            port = 8080;
        }

    }

    @Test
    public void test_Get_Statistics_200_OK() throws URISyntaxException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        URI transactionsUri = new URI(baseURL + port + "/transactions");
        testRestTemplate.postForEntity(transactionsUri, transaction1, Object.class).getStatusCode();

        URI statisticsUri = new URI(baseURL + port + "/statistics");
        assertEquals(HttpStatus.OK, testRestTemplate.getForEntity(statisticsUri, Object.class).getStatusCode());

    }

    @Test
    public void test_Get_Statistics_204_NO_CONTENT() throws URISyntaxException {

        URI transactionsUri = new URI(baseURL + port + "/transactions");
        testRestTemplate.delete(transactionsUri);

        URI statisticsUri = new URI(baseURL + port + "/statistics");
        assertEquals(HttpStatus.NO_CONTENT, testRestTemplate.getForEntity(statisticsUri, Object.class).getStatusCode());

    }

}
