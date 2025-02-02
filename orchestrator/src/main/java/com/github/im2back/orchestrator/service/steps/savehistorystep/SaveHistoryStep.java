package com.github.im2back.orchestrator.service.steps.savehistorystep;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;

public interface SaveHistoryStep {

	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO dto,List<StockUpdateResponseDTO> stockUpdateResponseDTOList) throws JsonProcessingException;
}
