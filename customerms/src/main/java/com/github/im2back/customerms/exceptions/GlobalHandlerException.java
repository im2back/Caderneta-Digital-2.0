package com.github.im2back.customerms.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;
import com.github.im2back.customerms.validations.exceptions.PurchaseValidationException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(CustomerNotFoundException.class)
	ResponseEntity<StandardError> customerNotFoundException(CustomerNotFoundException ex, HttpServletRequest request) {

		StandardError response = new StandardError(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<StandardErrorBeanValidation> methodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request, BindingResult bidingResult) {
		List<String> messages = new ArrayList<>();

		bidingResult.getFieldErrors().stream().forEach(e -> messages.add(e.getField() + " : " + e.getDefaultMessage()));

		StandardErrorBeanValidation response = new StandardErrorBeanValidation(HttpStatus.BAD_REQUEST.value(),
				"Bad Request", messages, request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	}
	
	@ExceptionHandler(CustomerRegisterValidationException.class)
	ResponseEntity<StandardErrorBeanValidation> customerRegisterValidations(CustomerRegisterValidationException ex,
			HttpServletRequest request) {
		
		StandardErrorBeanValidation response = new StandardErrorBeanValidation(
				HttpStatus.CONFLICT.value(),
				"Error at register customer",
				ex.getErrorMessages(), 
				request.getRequestURI());
		
		System.out.println(ex.getErrorMessages());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

	}
	
	@ExceptionHandler(PurchaseValidationException.class)
	ResponseEntity<StandardError> PurchaseValidationException(PurchaseValidationException ex, HttpServletRequest request) {

		StandardError response = new StandardError(HttpStatus.CONFLICT.value(), "Error at Purchase", ex.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

	}

}
