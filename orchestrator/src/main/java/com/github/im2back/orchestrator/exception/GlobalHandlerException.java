package com.github.im2back.orchestrator.exception;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.im2back.orchestrator.clients.exception.FeignClientCustomException;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;
import com.github.im2back.orchestrator.exception.customexceptions.GenericException;
import com.github.im2back.orchestrator.exception.customexceptions.HalfOpenCustomException;

import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(FeignClientCustomException.class)
	public ResponseEntity<StandardError> customFeignException(FeignClientCustomException exception,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<>();
		exception.getResponseBodyParsed().getMessages()
				.forEach(t -> messages.add("Erro ao executar o método: " + exception.getMethod()
						+ " que diparou uma requisição para o path " + exception.getResponseBodyParsed().getPath()
						+ ". Causa: " + t));

		StandardError body = new StandardError();
		body.setError("Erro Na Requisição Feign: " + exception.getResponseBodyParsed().getError());
		body.setStatus(exception.getStatus());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);

		return ResponseEntity.status(exception.getStatus()).body(body);
	}
		
	@ExceptionHandler(UnknownHostException.class)
	public ResponseEntity<StandardError> UnknownfHostException(UnknownHostException exception, HttpServletRequest request) {
		//"Service unavailable for: " + methodKey + "Cause: " + responseBody, 503, methodKey);
				
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Service Unavailable");
		body.setStatus(503);
		body.setPath(request.getRequestURI());
		body.setMessages(messages);
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
	}
	
	@ExceptionHandler(RetryableException.class)
	public ResponseEntity<StandardError> retryableException(RetryableException exception, HttpServletRequest request) {
		//"Service unavailable for: " + methodKey + "Cause: " + responseBody, 503, methodKey);
				
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Service Unavailable");
		body.setStatus(503);
		body.setPath(request.getRequestURI());
		body.setMessages(messages);
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
	}
	
	@ExceptionHandler(ServiceUnavailableCustomException.class)
	public ResponseEntity<StandardError> serviceUnavailableCustomException(ServiceUnavailableCustomException exception,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Service Unavailable");
		body.setStatus(exception.getStatus());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
	}	
	
	@ExceptionHandler(AsynchronousProcessingException.class)
	public ResponseEntity<StandardError> asynchronousProcessingException(AsynchronousProcessingException exception,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Processing Delayed");
		body.setStatus(exception.getStatus());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(body);
	}	
	
	@ExceptionHandler(HalfOpenCustomException.class)
	public ResponseEntity<PurchaseHistoryResponseDTO> halfOpenCustomException(HalfOpenCustomException ex,
			HttpServletRequest request) {

		PurchaseHistoryResponseDTO body =  ex.getPurchaseHistoryResponseDTO();
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}	
	
	@ExceptionHandler(CircuitBreakerCustomException.class)
	public ResponseEntity<StandardError> circuitBreakerCustomException(CircuitBreakerCustomException exception,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Processing Delayed");
		body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
	
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<StandardError> genericServiceException(GenericException exception,
			HttpServletRequest request) {

		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());

		StandardError body = new StandardError();
		body.setError("Processing Delayed");
		body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
}
