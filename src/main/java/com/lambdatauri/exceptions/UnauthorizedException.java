package com.lambdatauri.exceptions;

import com.lambdatauri.security.pojo.ResponseCodes;

public class UnauthorizedException  extends ApplicationCodedException {

    public UnauthorizedException(String message) {
        super(message, ResponseCodes.UNAUTHORIZED);
    }

    public UnauthorizedException(String message,Integer code, Throwable cause) {
        super(message,ResponseCodes.UNAUTHORIZED, cause);
    }

    public static UnauthorizedException invalidCredentials() {
        throw new UnauthorizedException("Invalid Credentials");
    }
}
