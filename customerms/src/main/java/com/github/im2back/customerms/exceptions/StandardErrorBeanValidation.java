package com.github.im2back.customerms.exceptions;

import java.util.List;

public record StandardErrorBeanValidation(
		Integer status, String error, List<String> message, String path

		
		) {

}
