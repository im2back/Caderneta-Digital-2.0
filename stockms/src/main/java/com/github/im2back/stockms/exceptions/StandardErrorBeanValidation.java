package com.github.im2back.stockms.exceptions;

import java.util.List;

public record StandardErrorBeanValidation(

		Integer status, String error, List<String> messages, String path

) {

}
