package com.github.im2back.orchestrator.service.steps.updatestockstep.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;
import com.github.im2back.orchestrator.service.circuitbreaker.manager.RedisCircuitBreakerStateManager;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.service.steps.updatestockstep.UpdateStockStep;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;


@Service
@Primary
@RequiredArgsConstructor
public class UpdateStockStepImplementationV1 implements UpdateStockStep {
		
	private final StockClient stockClient;
	private final CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	private final RedisCircuitBreakerStateManager redisCircuitBreakerManager;
	
	@Qualifier("circuitBreakerStockClient")
	private final CircuitBreaker circuitBreakerStockClient;
	@Qualifier("retryStockClient")
	private final Retry retryStockClient;
	
	@Override
	public List<StockResponseDTO> execute(PurchaseRequestDTO purchaseRequestDTO) throws JsonProcessingException {
		
		String currentState = redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient,"circuitBreakerStockClient");
		
		switch (currentState) {
			case "CLOSED" -> {
				redisCircuitBreakerManager.closedEvaluateCircuitTransition("circuitBreakerStockClient");
				try {
					List<StockResponseDTO> response = updateStockFaultToleranceContext(purchaseRequestDTO);
					redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerStockClient");
						return response;
				} catch (Exception e) {
					return fallBackMethod(purchaseRequestDTO, e);
				}
			}
			case "HALF_OPEN" -> {
				redisCircuitBreakerManager.halfOpenEvaluateCircuitTransition("circuitBreakerStockClient");
				try {
					List<StockResponseDTO> response = updateStockFaultToleranceContextCircuitOnly(purchaseRequestDTO);
					redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerStockClient");
						return response;
				} catch (Exception e) {
					return fallBackMethod(purchaseRequestDTO, e);
				}
			}
			case "OPEN" -> {
				return fallBackMethod(purchaseRequestDTO, null);
			}
			default -> throw new CircuitBreakerCustomException("Stock Step - Status desconhecido: " + currentState);
		}		
	}
	
	private List<StockResponseDTO> updateStockFaultToleranceContext(PurchaseRequestDTO purchaseRequestDTO)throws Exception {
		return Retry.decorateCallable(retryStockClient, circuitBreakerStockClient.decorateCallable(() -> {
			ResponseEntity<List<StockResponseDTO>> responseStockClient = stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems());
			return responseStockClient.getBody(); })).call();
	}
	
	private List<StockResponseDTO> updateStockFaultToleranceContextCircuitOnly(PurchaseRequestDTO purchaseRequestDTO)throws Exception {
		List <StockResponseDTO> response = CircuitBreaker.decorateCallable(circuitBreakerStockClient, () -> {
			ResponseEntity<List<StockResponseDTO>> responseStockClient = stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems());
				return responseStockClient.getBody(); }).call();
		return response;
	}
	
	public List<StockResponseDTO> fallBackMethod(PurchaseRequestDTO dto, Throwable e) throws JsonProcessingException {
		redisCircuitBreakerManager.updateRequestResultsInRedis("0","circuitBreakerStockClient");
		
 		List<StockResponseDTO> list = null;
 		String localCircuitBreakerState = circuitBreakerStockClient.getState().name();
	 	circuitBreakerStrategyContext.setStrategy(localCircuitBreakerState+"_CIRCUITBREAKERSTOCKCLIENT");
	 	circuitBreakerStrategyContext.executeStrategy(dto,list, e);	
	 	
	 	throw new CircuitBreakerCustomException("Erro no FallBack do StepStock: " + e.getMessage());
	}	

}
