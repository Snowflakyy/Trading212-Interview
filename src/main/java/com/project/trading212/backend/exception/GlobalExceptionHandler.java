package com.project.trading212.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFundsException(InsufficientFundsException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());

        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(BAD_REQUEST_ERROR).errorMessage(INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<Object> handleInsufficientQuantityException(InsufficientQuantityException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());
        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(BAD_REQUEST_ERROR).errorMessage(INSUFFICIENT_QUANTITY_EXCEPTION_MESSAGE).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchWalletException.class)
    public ResponseEntity<Object> handleNoSuchWalletException(NoSuchWalletException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());
        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(BAD_REQUEST_ERROR).errorMessage(NO_SUCH_WALLET_EXCEPTION_MESSAGE).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchCryptoException.class)
    public ResponseEntity<Object> handleNoSuchCryptoException(NoSuchCryptoException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());
        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(BAD_REQUEST_ERROR).errorMessage(NO_SUCH_CRYPTO_EXCEPTION_MESSAGE).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchTransactionException.class)
    public ResponseEntity<Object> handleNoSuchTransactionException(NoSuchTransactionException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());
        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(BAD_REQUEST_ERROR).errorMessage(NO_SUCH_TRANSACTION_EXCEPTION_MESSAGE).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        String errorMessageDescription = checkIfErrorMessageDescriptionIsValid(ex.getMessage());
        CustomError errorMessage = CustomError.builder().timestamp(formatter.format(LocalDateTime.now())).errorCode(INTERNAL_SERVER_ERROR).description(errorMessageDescription).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String checkIfErrorMessageDescriptionIsValid(String errorMessage) {
        return errorMessage == null ? "" : errorMessage;
    }
}
