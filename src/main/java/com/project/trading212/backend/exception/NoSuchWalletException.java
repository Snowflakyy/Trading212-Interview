package com.project.trading212.backend.exception;

public class NoSuchWalletException extends RuntimeException {
    public NoSuchWalletException(String message) {
        super(message);
    }
}
