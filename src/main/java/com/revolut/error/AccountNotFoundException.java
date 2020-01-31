package com.revolut.error;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class AccountNotFoundException extends TransferException {


    public AccountNotFoundException(Throwable throwable, String code, String message) {
        super(code, HTTP_NOT_FOUND, throwable, message);
    }

    public AccountNotFoundException(String code, String message) {
        super(code, HTTP_NOT_FOUND, null, message);
    }
}
