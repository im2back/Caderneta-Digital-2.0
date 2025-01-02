package com.github.im2back.validation.exception;

import java.util.List;

public record StandardError(int status, String error, List<String> messages, String path) {

}
