package com.github.im2back.stockms.exceptions;

public record CustomErrorResponse(int status, String error, String message, String path) {

}
