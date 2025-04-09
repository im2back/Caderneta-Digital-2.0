package com.github.im2back.stockms.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalHandlerExceptions {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<StandardError> productNotFoundException(ProductNotFoundException ex,HttpServletRequest request) {
		List<String> messages = new ArrayList<>();
		messages.add(ex.getMessage());
		
		StandardError response = new StandardError(
				HttpStatus.NOT_FOUND.value(),
				"Not Found", 
				messages,
				request.getRequestURI());
		response.messages().add(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
		

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request, BindingResult bindingResult) {
		
		List<String> messages = new ArrayList<>();
		bindingResult.getFieldErrors().stream()
				.forEach(fieldError -> messages.add(fieldError.getField() + " : " + fieldError.getDefaultMessage()));

		StandardError response = new StandardError(HttpStatus.BAD_REQUEST.value(),
				"Bad Request", messages, request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<StandardError> customClientException(HttpClientErrorException ex,HttpServletRequest request) {
		
		List<String> messages = new ArrayList<>();
		messages.add(ex.getMessage());
		
		StandardError response = new StandardError(
				ex.getStatusCode().value(), 
				"Error Feing Client",
				messages,
				request.getRequestURI());
		
		return ResponseEntity.status(ex.getStatusCode()).body(response);
	}
		
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex,HttpServletRequest request) {
		
		List<String> messages = new ArrayList<>();
		messages.add(ex.getRootCause().getMessage());
		
		StandardError response = new StandardError(
				HttpStatus.CONFLICT.value(), 
				"Data Integrity Violation",
				messages,
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> constraintViolationException(ConstraintViolationException ex,HttpServletRequest request) {
		
		List<String> messages = new ArrayList<>();
		messages.add(ex.getMessage());
		
		StandardError response = new StandardError(
				HttpStatus.BAD_REQUEST.value(), 
				"BAD REQUEST",
				messages,
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
}
