package com.github.im2back.orchestrator.service.steps.validationstep.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.ValidationClient;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.service.steps.validationstep.ValidationStep;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class ValidationStepImplementationV1 implements ValidationStep {
	
	private  final ValidationClient validationClient;
	
	@SuppressWarnings("unused")
	@Override
	@Retry(name = "retryValidationClient")
	public void execute(PurchaseRequestDTO dto) {
		ResponseEntity<Void> responseRequestValidation =  validationClient.valid(dto);		
	}
	
}
