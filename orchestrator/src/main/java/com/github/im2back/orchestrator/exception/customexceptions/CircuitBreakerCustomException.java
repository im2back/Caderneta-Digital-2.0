package com.github.im2back.orchestrator.exception.customexceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CircuitBreakerCustomException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	
}
