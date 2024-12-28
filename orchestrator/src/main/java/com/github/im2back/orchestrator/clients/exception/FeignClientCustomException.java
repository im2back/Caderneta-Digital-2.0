package com.github.im2back.orchestrator.clients.exception;

import com.github.im2back.orchestrator.exception.StandardError;

public class FeignClientCustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;
    private final StandardError responseBodyParsed;
    private final String method;
    
    public FeignClientCustomException(int status, StandardError responseBodyParsed, String method) {
        this.method = method;
        this.status = status;
        this.responseBodyParsed = responseBodyParsed;
    }

    public int getStatus() {
        return status;
    }

    public StandardError getResponseBodyParsed() {
        return responseBodyParsed;
    }
    
    public String getMethod() {
        return method;
    }
}