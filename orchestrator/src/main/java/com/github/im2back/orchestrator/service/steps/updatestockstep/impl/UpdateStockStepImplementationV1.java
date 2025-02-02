package com.github.im2back.orchestrator.service.steps.updatestockstep.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.service.steps.updatestockstep.UpdateStockStep;
import com.github.im2back.orchestrator.utils.Util;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;


@Service
@Primary
@RequiredArgsConstructor
public class UpdateStockStepImplementationV1 implements UpdateStockStep {
		
	private final StockClient stockClient;
	private final CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	private final CircuitBreakerRegistry circuitBreakerRegistry;
	private final RetryRegistry retryRegistry;
	
	@Override
	public List<StockUpdateResponseDTO> execute(PurchaseRequestDTO purchaseRequestDTO) throws JsonProcessingException {
		
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreakerStockClient"); 
		Retry retry = retryRegistry.retry("retryStockClient");	
		String circuitBreakerState = Util.getCircuitBreakerState(circuitBreakerRegistry, "circuitBreakerStockClient"); 
		
		switch (circuitBreakerState) {
			case "CLOSED", "HALF_OPEN" -> {
				try {
					return  executeWithCircuitBreakerAndRetry(purchaseRequestDTO, circuitBreaker, retry);
				} catch (Exception e) {
					return fallBackMethod(purchaseRequestDTO, e);
				}
			}
	
			case "OPEN" -> {
				return fallBackMethod(purchaseRequestDTO, null);
			}
			default -> throw new CircuitBreakerCustomException("Status desconhecido: " + circuitBreakerState);
		}		
	}
	
	public List<StockUpdateResponseDTO> fallBackMethod(PurchaseRequestDTO dto, Throwable e) throws JsonProcessingException {
 	var circuitBreakerState = Util.getCircuitBreakerState(circuitBreakerRegistry,"circuitBreakerStockClient");	
	 	
 		List<StockUpdateResponseDTO> list = null;// dado mocado para preencher assinatura revisar
 	
	 	circuitBreakerStrategyContext.setStrategy(circuitBreakerState+"_CIRCUITBREAKERSTOCKCLIENT");
	 	circuitBreakerStrategyContext.executeStrategy(dto,list, e);	
	 	
		return null; // retorno nulo apenas para compilar
	}
	
	private List<StockUpdateResponseDTO> executeWithCircuitBreakerAndRetry(PurchaseRequestDTO purchaseRequestDTO,
			CircuitBreaker circuitBreaker, Retry retry) throws Exception {
		return Retry.decorateCallable(retry, circuitBreaker.decorateCallable(() -> {
			ResponseEntity<List<StockUpdateResponseDTO>> responseStockClient = stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems());
			return responseStockClient.getBody(); })).call();
	}

}
