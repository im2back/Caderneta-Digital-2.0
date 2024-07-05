package com.github.im2back.stockms.exceptions;

public record StandardError(Integer status, String error, String message, String path) {

}
