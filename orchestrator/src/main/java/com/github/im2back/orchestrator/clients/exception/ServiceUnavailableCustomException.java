package com.github.im2back.orchestrator.clients.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUnavailableCustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int status;
	private String method;
	
}