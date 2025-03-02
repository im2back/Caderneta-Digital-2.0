package com.github.im2back.orchestrator.service.circuitbreaker.strategy;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;

public interface CircuitBreakerStrategyInterface {
	
	void execute(
			PurchaseRequestDTO purchaseRequestDTO,
			List<StockResponseDTO> stockUpdateResponseDTOList,
			Throwable e)
			throws JsonProcessingException;

}
