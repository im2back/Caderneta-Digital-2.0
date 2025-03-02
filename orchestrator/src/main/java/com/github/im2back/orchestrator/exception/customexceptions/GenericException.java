package com.github.im2back.orchestrator.exception.customexceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;

}
