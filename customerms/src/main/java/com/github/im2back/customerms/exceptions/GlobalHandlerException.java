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


import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(CustomerNotFoundException.class)
	ResponseEntity<StandardError> customerNotFoundException(CustomerNotFoundException ex, HttpServletRequest request) {
		List<String> messageErrosList = new ArrayList<>();
		messageErrosList.add(ex.getMessage());
		StandardError response = new StandardError(HttpStatus.NOT_FOUND.value(), "Not Found", messageErrosList,
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request, BindingResult bidingResult) {
		List<String> messages = new ArrayList<>();

		bidingResult.getFieldErrors().stream().forEach(e -> messages.add(e.getField() + " : " + e.getDefaultMessage()));

		StandardError response = new StandardError(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request", 
				messages, 
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(CustomerRegisterValidationException.class)
	ResponseEntity<StandardError> customerRegisterValidations(CustomerRegisterValidationException ex,HttpServletRequest request) {
		
		StandardError response = new StandardError(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Error at register customer",
				ex.getErrorMessages(), 
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	
}
