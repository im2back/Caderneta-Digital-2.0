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
import com.github.im2back.orchestrator.exception.StandardError;
import com.github.im2back.orchestrator.service.orchestrator.OrchestratorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


@RequestMapping("v1/orchestrator")
@RestController
@RequiredArgsConstructor
public class OrchestratorControllerImplV1 implements OrchestratorController {
	
	private final OrchestratorService orchestratorService;
	
	@Override
	@PostMapping("/purchase")
	@Operation(summary = "Endpoint for orchestrating purchases")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Returns a purchase DTO of type PurchaseHistoryResponseDTO",
	                content = @Content(mediaType = "application/json", 
	                                   schema = @Schema(implementation = PurchaseHistoryResponseDTO.class))),

	        @ApiResponse(responseCode = "422", description = "Business exception triggered by the validation module or a microservice",
	                content = @Content(mediaType = "application/json", 
	                                   schema = @Schema(implementation = StandardError.class))),

	        @ApiResponse(responseCode = "202", description = "Request sent for asynchronous processing",
	                content = @Content(mediaType = "application/json", 
	                                   schema = @Schema(implementation = StandardError.class))),

	        @ApiResponse(responseCode = "503", description = "Purchase canceled",
	                content = @Content(mediaType = "application/json", 
	                                   schema = @Schema(implementation = StandardError.class))),

	        @ApiResponse(responseCode = "500", description = "Internal server error",
	                content = @Content(mediaType = "application/json", 
	                                   schema = @Schema(implementation = StandardError.class)))
	})
	public ResponseEntity<PurchaseHistoryResponseDTO> orchestratePurchase(@RequestBody PurchaseRequestDTO dtoIn) throws JsonProcessingException {

		PurchaseHistoryResponseDTO response = orchestratorService.orchestratePurchase(dtoIn);
		return ResponseEntity.ok(response);
	}
	
}
