package com.lambdatauri.security.config;

import com.lambdatauri.security.pojo.ResponseCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class ExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {


    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        response.setStatus(status.value());
        var out = response.getWriter();
        response.setContentType("application/json");
        out.print(
                "{\"type\":\"about:blank\",\"title\":\"Unauthorized\",\"status\":%d,\"detail\":\"%s\",\"instance\":%s,\"code\":\"%s\"}"
                        .formatted(
                                status.value(),
                                UNAUTHORIZED.getReasonPhrase(),
                                request.getPathInfo(),
                                ResponseCodes.UNAUTHORIZED));
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        HttpStatus status = HttpStatus.FORBIDDEN;

        response.setStatus(status.value());
        var out = response.getWriter();
        response.setContentType("application/json");
        out.print(
                "{\"type\":\"about:blank\",\"title\":\"Forbidden\",\"status\":%d,\"detail\":\"%s\",\"instance\":%s,\"code\":\"%s\"}"
                        .formatted(
                                status.value(),
                                FORBIDDEN.getReasonPhrase(),
                                request.getPathInfo(),
                                ResponseCodes.FORBIDDEN));
    }
}