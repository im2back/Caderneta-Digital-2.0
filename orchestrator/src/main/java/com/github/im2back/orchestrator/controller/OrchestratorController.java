package com.github.im2back.orchestrator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.service.OrchestratorService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/orchestrator")
@RestController
@RequiredArgsConstructor
public class OrchestratorController {

	private final OrchestratorService orchestratorService;
	
	@PostMapping("/purchase")
	public ResponseEntity<Void> orchestratePurchase(@RequestBody PurchaseRequestDTO dto) {
		orchestratorService.orchestratePurchase(dto);
		return ResponseEntity.ok().build();
	}
	
}
