package com.github.im2back.orchestrator.clients.exception;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.exception.StandardError;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignErrorDecoder implements ErrorDecoder {
	
	@Override
	public Exception decode(String methodKey, Response response) {
		String responseBody = extractTheBodyInJsonFormat(response);
		 		
		switch (response.status()) {
		case 500,501,502,503:
			throw new ServiceUnavailableCustomException(
				"Service unavailable for: " + methodKey + "Cause: " + responseBody, 503, methodKey);
		
		default:
			StandardError responseBodyParsed = parseResponse(responseBody);
			throw new FeignClientCustomException(response.status(), responseBodyParsed, methodKey);
		}
	}

	private String extractTheBodyInJsonFormat(Response response) {
		String responseBody = null;
		
		if (response.body() != null) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
				responseBody = reader.lines().reduce("", String::concat);
			} catch (Exception e) {
				responseBody = "Erro ao processar o corpo da resposta.";
			}
		}
		return responseBody;
	}

	private StandardError parseResponse(String json) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			StandardError errorResponse = objectMapper.readValue(json, StandardError.class);
			return errorResponse;

		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Erro na conversão do body para StandardError.");
	}

}
