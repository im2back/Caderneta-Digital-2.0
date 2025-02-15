package com.github.im2back.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;

public interface OrchestratorController {

	public ResponseEntity<PurchaseHistoryResponseDTO> orchestratePurchase(@RequestBody PurchaseRequestDTO dtoIn) throws JsonProcessingException;
	
}
