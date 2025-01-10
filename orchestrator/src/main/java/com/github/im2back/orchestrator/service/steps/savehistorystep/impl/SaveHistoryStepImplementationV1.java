package com.github.im2back.orchestrator.service.steps.savehistorystep.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.CustomerClient;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.dto.out.UpdatedProducts;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;

import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class SaveHistoryStepImplementationV1 implements SaveHistoryStep {
	
	
	private final CustomerClient customerClient;
	
	@Override
	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO dto,List<StockUpdateResponseDTO> stockUpdateResponseDTOList) {
		List<UpdatedProducts> products = new ArrayList<>();
		PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);
		
		stockUpdateResponseDTOList.forEach(t -> {
			products.add(new UpdatedProducts(t.name(), t.price(), t.code(), t.quantity()));
		});

		ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(purchaseHistoryDTO);
		return responseCustomerClient.getBody();
	}

	
	
	
}
