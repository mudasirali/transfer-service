package com.revolut.error;

import lombok.Getter;

import java.util.Arrays;

@Getter
public abstract class TransferException extends RuntimeException {

    private int httpStatus;

    /**
     * used for translation
     */
    private String code;

    public TransferException(String code, int httpStatus, Throwable throwable, String message) {
        super(message, throwable);
        this.httpStatus = httpStatus;
        this.code = code;
    }


}
