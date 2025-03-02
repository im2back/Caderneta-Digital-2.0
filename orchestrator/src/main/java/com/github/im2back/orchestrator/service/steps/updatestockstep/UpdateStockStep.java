package com.github.im2back.orchestrator.service.steps.updatestockstep;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;

public interface UpdateStockStep {
	public List<StockResponseDTO> execute(PurchaseRequestDTO dto) throws JsonProcessingException;
	
}
