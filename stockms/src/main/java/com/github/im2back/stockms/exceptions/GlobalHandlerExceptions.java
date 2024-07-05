package com.github.im2back.stockms.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalHandlerExceptions {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<StandardError> productNotFoundException(ProductNotFoundException ex,
			HttpServletRequest request) {
		StandardError response = new StandardError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardErrorBeanValidation> methodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request, BindingResult bindingResult) {
		
		List<String> messages = new ArrayList<>();
		bindingResult.getFieldErrors().stream()
				.forEach(fieldError -> messages.add(fieldError.getField() + " : " + fieldError.getDefaultMessage()));

		StandardErrorBeanValidation response = new StandardErrorBeanValidation(HttpStatus.BAD_REQUEST.value(),
				"Bad Request", messages, request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
