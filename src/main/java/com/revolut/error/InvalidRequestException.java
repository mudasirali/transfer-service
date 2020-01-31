package com.revolut.error;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class InvalidRequestException extends TransferException {


    public InvalidRequestException(Throwable throwable, String code, String message) {
        super(code, HTTP_BAD_REQUEST, throwable, message);
    }

    public InvalidRequestException(String code, String message) {
        super(code, HTTP_BAD_REQUEST, null, message);
    }
}
