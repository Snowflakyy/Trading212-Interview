package com.project.trading212.backend.exception;

public class NoSuchTransactionException extends RuntimeException {
    public NoSuchTransactionException(String message) {
        super(message);
    }
}
