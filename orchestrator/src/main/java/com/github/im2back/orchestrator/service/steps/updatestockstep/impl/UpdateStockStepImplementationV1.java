package com.github.im2back.orchestrator.service.steps.updatestockstep.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.steps.updatestockstep.UpdateStockStep;

import lombok.RequiredArgsConstructor;


@Service
@Primary
@RequiredArgsConstructor
public class UpdateStockStepImplementationV1 implements UpdateStockStep {
	
	
	private final StockClient stockClient;

	@Override
	public List<StockUpdateResponseDTO> execute(PurchaseRequestDTO dto) {

		ResponseEntity<List<StockUpdateResponseDTO>> responseStockClient = stockClient
				.updateStockAfterPurchase(dto.purchasedItems());
		List<StockUpdateResponseDTO> stockUpdateResponseDTOList = responseStockClient.getBody();
		return stockUpdateResponseDTOList;

	}

}
