package com.project.trading212.backend.exception;

public class ErrorCodeAndMessages {
    public static final String INSUFFICIENT_FUNDS_EXCEPTION_DESCRIPTION = "Insufficient Funds";
    public static final String INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE = "Error during buy transaction";
    public static final String INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION = "Insufficient Quantity";
    public static final String INSUFFICIENT_QUANTITY_EXCEPTION_MESSAGE = "Error during sell transaction";
    public static final String NO_SUCH_WALLET_EXCEPTION_DESCRIPTION = "No Such Wallet";
    public static final String NO_SUCH_WALLET_EXCEPTION_MESSAGE = "Error during wallet fetching";
    public static final String NO_SUCH_CRYPTO_EXCEPTION_DESCRIPTION = "No Such Crypto";
    public static final String NO_SUCH_CRYPTO_EXCEPTION_MESSAGE = "Error during CRYPTO fetching";
    public static final String NO_SUCH_TRANSACTION_EXCEPTION_DESCRIPTION = "No such transaction";
    public static final String NO_SUCH_TRANSACTION_EXCEPTION_MESSAGE = "Error during Transaction fetching";

    public static final String BAD_REQUEST_ERROR = "400 - Bad Request";
    public static final String INTERNAL_SERVER_ERROR = "500 - Internal Server Error";
    public static final String SYSTEM_ERROR_MESSAGE = "System error";
}
