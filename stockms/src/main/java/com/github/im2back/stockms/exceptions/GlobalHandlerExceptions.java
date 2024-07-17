package com.github.im2back.stockms.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;
import com.github.im2back.stockms.validation.exceptions.PurchaseValidationException;

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
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<StandardError> customClientException(HttpClientErrorException ex,
			HttpServletRequest request) {

		StandardError response = new StandardError(
				ex.getStatusCode().value(), 
				"Error Feing Client",
				ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(ex.getStatusCode()).body(response);
	}
	
	@ExceptionHandler(PurchaseValidationException.class)
	public ResponseEntity<StandardErrorBeanValidation> purchaseValidationException(PurchaseValidationException ex,
			HttpServletRequest request) {
		
		StandardErrorBeanValidation response = new StandardErrorBeanValidation(
				HttpStatus.BAD_REQUEST.value(), 
				"Purchase Error",
				ex.getErrorMessages(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex,
			HttpServletRequest request) {

		
		StandardError response = new StandardError(
				HttpStatus.CONFLICT.value(), 
				"Data Integrity Violation ",
				ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
}
