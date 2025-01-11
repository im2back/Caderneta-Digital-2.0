package com.github.im2back.orchestrator.service.steps.savehistorystep.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.CustomerClient;
import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.dto.out.UpdatedProducts;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class SaveHistoryStepImplementationV1 implements SaveHistoryStep {
	
	private final StockClient stockClient;
	private final CustomerClient customerClient;
	
	@Override
	@Retry(name = "retryCustomerClient")
	@CircuitBreaker(name = "circuitBreakerCustomerClient", fallbackMethod = "fallbackMethod")
	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO dto,List<StockUpdateResponseDTO> stockUpdateResponseDTOList) {
		List<UpdatedProducts> products = new ArrayList<>();
		PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);
		
		stockUpdateResponseDTOList.forEach(t -> {
			products.add(new UpdatedProducts(t.name(), t.price(), t.code(), t.quantity()));
		});

		ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(purchaseHistoryDTO);
		return responseCustomerClient.getBody();
	}
	
	public PurchaseHistoryResponseDTO fallbackMethod(PurchaseRequestDTO dto,List<StockUpdateResponseDTO> stockUpdateResponseDTOList, Throwable e) {		
			var response = stockClient.massiveReplenishmentInStock(stockUpdateResponseDTOList);
			
			if(response != null) {
				throw new ServiceUnavailableCustomException("Compra revertida. Causa: "+e.getMessage(), 503, null);
			}
			// retorno nulo apenas para compilar
			return null;
	}
	

	
	
	
}
