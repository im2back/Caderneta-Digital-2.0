package com.github.im2back.customerms.exceptions;

import java.util.List;

public record StandardError(Integer status, String error, List<String> messages, String path

) {

}
