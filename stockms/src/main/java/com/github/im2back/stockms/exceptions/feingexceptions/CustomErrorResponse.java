package com.github.im2back.stockms.exceptions.feingexceptions;

public record CustomErrorResponse(int status, String error, String message, String path) {

}
