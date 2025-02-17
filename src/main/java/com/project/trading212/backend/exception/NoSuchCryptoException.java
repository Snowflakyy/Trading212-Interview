package com.project.trading212.backend.exception;

public class NoSuchCryptoException extends RuntimeException {
    public NoSuchCryptoException(String message) {
        super(message);
    }
}
