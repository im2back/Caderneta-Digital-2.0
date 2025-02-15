package com.github.im2back.orchestrator.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.controller.OrchestratorController;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.service.orchestrator.OrchestratorService;

import lombok.RequiredArgsConstructor;


@RequestMapping("v1/orchestrator")
@RestController
@RequiredArgsConstructor
public class OrchestratorControllerImplV1 implements OrchestratorController {
	
	private final OrchestratorService orchestratorService;
	
	@Override
	@PostMapping("/purchase")
	public ResponseEntity<PurchaseHistoryResponseDTO> orchestratePurchase(@RequestBody PurchaseRequestDTO dtoIn) throws JsonProcessingException {

		PurchaseHistoryResponseDTO response = orchestratorService.orchestratePurchase(dtoIn);
		return ResponseEntity.ok(response);
	}
	
}
