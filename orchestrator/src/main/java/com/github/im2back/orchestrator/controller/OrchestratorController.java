package com.github.im2back.orchestrator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.service.orchestrator.OrchestratorService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/orchestrator")
@RestController
@RequiredArgsConstructor
public class OrchestratorController {

	private final OrchestratorService orchestratorService;
	
	@Value("${instance.id}")
    private String nonSecurePort;
	
	@PostMapping("/purchase")
	public ResponseEntity<PurchaseHistoryResponseDTO> orchestratePurchase(@RequestBody PurchaseRequestDTO dtoIn) throws JsonProcessingException {
		System.out.println("Porta registrada no Eureka: " + nonSecurePort);
		PurchaseHistoryResponseDTO response =orchestratorService.orchestratePurchase(dtoIn);
		return ResponseEntity.ok(response);
	}
	
}
