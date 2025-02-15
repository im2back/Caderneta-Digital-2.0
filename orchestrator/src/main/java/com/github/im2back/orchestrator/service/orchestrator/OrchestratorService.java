package com.github.im2back.orchestrator.service.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;


public interface OrchestratorService {

	public PurchaseHistoryResponseDTO orchestratePurchase(PurchaseRequestDTO dto) throws JsonProcessingException;

}
