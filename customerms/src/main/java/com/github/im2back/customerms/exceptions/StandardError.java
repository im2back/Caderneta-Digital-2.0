package com.github.im2back.customerms.exceptions;

public record StandardError(Integer status, String error, String message, String path

) {

}
