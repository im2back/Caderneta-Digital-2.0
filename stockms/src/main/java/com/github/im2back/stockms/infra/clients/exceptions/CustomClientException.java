package com.github.im2back.stockms.infra.clients.exceptions;

public class CustomClientException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CustomClientException(String msg) {
		super(msg);
	}
}
