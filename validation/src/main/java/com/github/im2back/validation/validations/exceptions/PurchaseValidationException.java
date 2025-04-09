package com.github.im2back.validation.validations.exceptions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final List<String> errorMessages;

	public PurchaseValidationException(List<String> errorMessages) {
		super("Purchase validation failed");
		this.errorMessages = errorMessages;
	}

}
