package com.github.im2back.validation.service.exceptions;

public class CustomerNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String msg) {
	super(msg);
	}
}