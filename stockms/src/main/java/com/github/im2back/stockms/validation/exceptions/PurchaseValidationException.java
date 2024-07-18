package com.github.im2back.stockms.validation.exceptions;

import java.util.List;

public class PurchaseValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private List<String> errorMessages;

	public PurchaseValidationException(List<String> errorMessages) {
		super("Purchase validation failed");
		this.errorMessages = errorMessages;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
}
