package com.github.im2back.validation.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(PurchaseValidationException.class)
	ResponseEntity<StandardError> PurchaseValidationException(PurchaseValidationException ex, HttpServletRequest request) {
		
		List<String> erros = new ArrayList<>();
		ex.getErrorMessages().forEach(t ->{
			erros.add(t);
		});
		
		StandardError response = new StandardError(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Error at Purchase", 
				erros,
				request.getRequestURI());

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);

	}
	
}
