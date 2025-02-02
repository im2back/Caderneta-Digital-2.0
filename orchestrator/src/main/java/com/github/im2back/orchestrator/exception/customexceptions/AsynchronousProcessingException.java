package com.github.im2back.orchestrator.exception.customexceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsynchronousProcessingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String message;
	private int status;
	public AsynchronousProcessingException (String message, int status) {
	this.message = message;
	this.status =  status;
	}

	
}
