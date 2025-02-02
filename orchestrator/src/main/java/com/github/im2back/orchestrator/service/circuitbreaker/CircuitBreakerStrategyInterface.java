package com.github.im2back.orchestrator.service.circuitbreaker;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;

public interface CircuitBreakerStrategyInterface {
	
	void execute(
			PurchaseRequestDTO purchaseRequestDTO,
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList,
			Throwable e)
			throws JsonProcessingException;

}
