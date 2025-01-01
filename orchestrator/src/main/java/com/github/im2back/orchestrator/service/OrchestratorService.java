package com.github.im2back.orchestrator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.CustomerClient;
import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.ValidationClient;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.dto.out.UpdatedProducts;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

	private final StockClient  stockClient;
	private final CustomerClient customerClient;
	private final ValidationClient validationClient;
	
	public PurchaseHistoryResponseDTO orchestratePurchase(PurchaseRequestDTO dto) {
		
		//Etapa 1 - Validar compra antes de processar
		ResponseEntity<Void> responseRequestValidation = validationClient.valid(dto);
		
		
		//Etapa 2 - Atualizar estoque e deduzir a quantidade de produtos comprados
			ResponseEntity<List<StockUpdateResponseDTO>> responseStockClient = stockClient.updateStock(dto.purchasedItems());
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList = responseStockClient.getBody();
			
		//Etapa 3 - Receber a resposta da etapa 1, montar um purchaseHistoryDTO e envialo para o customer-ms persistir um historico da compra.
			List<UpdatedProducts> products = new ArrayList<>();
			PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);
			
			stockUpdateResponseDTOList.forEach(t -> {
				products.add(new UpdatedProducts(t.name(), t.price(), t.code(), t.quantity()));
			});
			
			ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(purchaseHistoryDTO);
			return responseCustomerClient.getBody();
	}

	
	
}
