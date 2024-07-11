package com.github.im2back.stockms.exceptions;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		CustomErrorResponse errorResponse = null;

		try (InputStream inputStream = response.body().asInputStream()) {
			if (inputStream != null) {
				errorResponse = objectMapper.readValue(inputStream, CustomErrorResponse.class);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new HttpClientErrorException(HttpStatus.valueOf(errorResponse.status()), errorResponse.message());
	}

}
