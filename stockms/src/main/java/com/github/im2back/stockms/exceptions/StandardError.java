package com.github.im2back.stockms.exceptions;

import java.util.List;

public record StandardError(Integer status, String error, List<String> messages, String path) {

}
