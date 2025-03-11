package com.github.im2back.orchestrator.util;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.orchestrator.clients.exception.FeignClientCustomException;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.exception.StandardError;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;

public class ExceptionTestFactory {

	public static FeignClientCustomException createFeignClientCustomExeptionState422() {
		List<String> messages = new ArrayList<>();
		StandardError standardError = new StandardError(422, "Erro de negocio", messages, "/path");
		return new FeignClientCustomException(422, standardError, "method");
	}
	
	public static RuntimeException createRuntimeException() {
		return new RuntimeException();
	}
	
	public static FeignClientCustomException createFeignClientCustomExceptionState400() {
		List<String> messages = new ArrayList<>();
		StandardError standard = new StandardError(400, "error", messages, "/path");
		return new FeignClientCustomException(400, standard, "method");
	}
	
	public static ServiceUnavailableCustomException createServiceUnavailableCustomException() {
		return new ServiceUnavailableCustomException();
	}
	
	public static AsynchronousProcessingException  createAsynchronousProcessingException() {
		return new AsynchronousProcessingException("Enviado para processamento assincrono", 202);
	}
}
