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

public class TransactionControllerTests {

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
    public void test_Add_Transactions_200_OK() throws URISyntaxException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), String.valueOf(OffsetDateTime.now(ZoneOffset.UTC)));
        URI uri = new URI(baseURL + port + "/transactions");
        assertEquals(HttpStatus.CREATED, testRestTemplate.postForEntity(uri, transaction1, Object.class).getStatusCode());

    }

    @Test
    public void test_Add_Transactions_204_NO_CONTENT() throws URISyntaxException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), "0001-08-01T13:39:00.431563200Z");
        URI uri = new URI(baseURL + port + "/transactions");
        assertEquals(HttpStatus.NO_CONTENT, testRestTemplate.postForEntity(uri, transaction1, Object.class).getStatusCode());

    }

    @Test
    public void test_Add_Transactions_422_Timestamp_Field_Not_Parsable() throws URISyntaxException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), "0001-08-0113:39:00.431563200Z");
        URI uri = new URI(baseURL + port + "/transactions");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, testRestTemplate.postForEntity(uri, transaction1, String.class).getStatusCode());
    }

    @Test
    public void test_Add_Transactions_422_Future_Timestamp_Not_Allowed() throws URISyntaxException {

        Transaction transaction1 = new Transaction(new BigDecimal("2130.345"), "9999-08-0113:39:00.431563200Z");
        URI uri = new URI(baseURL + port + "/transactions");
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, testRestTemplate.postForEntity(uri, transaction1, String.class).getStatusCode());
    }

    @Test
    public void test_Delete_Transactions() throws URISyntaxException {

        URI uri = new URI(baseURL + port + "/transactions");
        testRestTemplate.delete(uri);
    }

}
