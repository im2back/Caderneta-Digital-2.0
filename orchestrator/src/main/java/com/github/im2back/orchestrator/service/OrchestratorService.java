package com.github.im2back.orchestrator.service;

import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

	private final StockClient  stockClient;
	
	public void orchestratePurchase(PurchaseRequestDTO dto) {
		//Etapa 1 - Atualizar estoque e deduzir a quantidade de produtos comprados
			stockClient.updateStock(dto.purchasedItems());

	}

	
	
}
