package com.github.im2back.orchestrator.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.im2back.orchestrator.clients.exception.FeignClientCustomException;

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
		body.setError("Erro na requisição open feign: " + exception.getResponseBodyParsed().getError());
		body.setStatus(exception.getStatus());
		body.setPath(request.getRequestURI());
		body.setMessages(messages);

		return ResponseEntity.status(exception.getStatus()).body(body);
	}

}
