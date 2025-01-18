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
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class SaveHistoryStepImplementationV1 implements SaveHistoryStep {
	
	private final CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	private final CustomerClient customerClient;
	private final CircuitBreakerRegistry circuitBreakerRegistry;
	
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
	 	var circuitBreakerState = getCircuitBreakerState("circuitBreakerCustomerClient");	
		
	 	circuitBreakerStrategyContext.setStrategy(circuitBreakerState);
	 	circuitBreakerStrategyContext.executeStrategy(stockUpdateResponseDTOList, e);			
		return null; // retorno nulo apenas para compilar
	}
	
	public String getCircuitBreakerState(String circuitBreakerName) {    
	    io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
	    // Retorna o estado (CLOSED, OPEN, HALF_OPEN)
	    return circuitBreaker.getState().name();
	}	
}
