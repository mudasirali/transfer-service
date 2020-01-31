package com.revolut.error;

import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class InsufficientFundsException extends TransferException {


    public InsufficientFundsException(Throwable throwable, String code, String message) {
        super(code, HTTP_CONFLICT, throwable, message);
    }

    public InsufficientFundsException(String code, String message) {
        super(code, HTTP_CONFLICT, null, message);
    }
}
