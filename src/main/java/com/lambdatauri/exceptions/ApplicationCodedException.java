package com.lambdatauri.exceptions;

import com.lambdatauri.security.pojo.ResponseCodes;

public class ApplicationCodedException extends RuntimeException {

    private final int code;

    public ApplicationCodedException(String message, ResponseCodes code) {
        super(message);
        this.code = code.getCode();
    }

    public ApplicationCodedException(String message, ResponseCodes code, Throwable cause) {
        super(message, cause);
        this.code = code.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
