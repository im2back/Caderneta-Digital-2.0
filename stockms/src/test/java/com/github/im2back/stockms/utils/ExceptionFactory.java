package com.github.im2back.stockms.utils;

import org.springframework.dao.DataIntegrityViolationException;

import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;

import jakarta.validation.ConstraintViolationException;

public class ExceptionFactory {
	
	public static ProductNotFoundException createProductNotFoundException() {
		return new ProductNotFoundException("msg");
	}
	
	public static ConstraintViolationException createConstraintViolationException() {
		return new ConstraintViolationException(null);
	}
	

	public static DataIntegrityViolationException dataIntegrityViolationException() {
		 return new DataIntegrityViolationException("teste", new RuntimeException("Código duplicado"));
	//	return new DataIntegrityViolationException("teste");
	}
	
}
