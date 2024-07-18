package com.github.im2back.customerms.validations.exceptions;

import java.util.List;

public class CustomerRegisterValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private List<String> errorMessages;

	public CustomerRegisterValidationException(List<String> errorMessages) {
		super("");
		this.errorMessages = errorMessages;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
}
