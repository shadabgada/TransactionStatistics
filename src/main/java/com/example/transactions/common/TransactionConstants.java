package com.example.transactions.common;

public class TransactionConstants {

    public static final String FUTURE_DATE_NOT_ALLOWED = "Future date is not allowed";

    public static final long EXPIRY_SECONDS = 60;

    public static final String NO_TRANSACTIONS_FOUND = "No transactions found in last " + EXPIRY_SECONDS + " seconds";

    public static final String TRANSACTION_EXPIRED = "Transaction older than last " + EXPIRY_SECONDS + " seconds not allowed";

}
