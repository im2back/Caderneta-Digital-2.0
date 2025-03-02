package com.github.im2back.orchestrator.service.steps.savehistorystep.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.CustomerClient;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;
import com.github.im2back.orchestrator.service.circuitbreaker.manager.RedisCircuitBreakerStateManager;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;
import com.github.im2back.orchestrator.service.utils.Utils;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class SaveHistoryStepImplementationV1 implements SaveHistoryStep {

	private final CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	private final CustomerClient customerClient;
	private final RedisCircuitBreakerStateManager redisCircuitBreakerManager;
	@Qualifier("circuitBreakerCustomerClient")
	private final CircuitBreaker circuitBreakerCustomerClient;
	@Qualifier("retryCustomerClient")
	private final Retry retryCustomerClient;
	
	@Override
	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO purchaseRequestDTO,List<StockResponseDTO> stockResponseList) 
			throws JsonProcessingException {
	
		String currentState = redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient,"circuitBreakerCustomerClient");

		switch (currentState) {
			case "CLOSED" -> {
				redisCircuitBreakerManager.closedEvaluateCircuitTransition("circuitBreakerCustomerClient");
				try {	
					PurchaseHistoryResponseDTO response = saveHistoryFaultToleranceContext(purchaseRequestDTO, stockResponseList);
				    redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerCustomerClient");
						return response;					
			   } catch (Exception e) {
						return fallBackMethod(purchaseRequestDTO, stockResponseList, e);
				}
			}		
			case "HALF_OPEN" -> {
				redisCircuitBreakerManager.halfOpenEvaluateCircuitTransition("circuitBreakerCustomerClient");		
				try {
					PurchaseHistoryResponseDTO response = saveHistoryFaultToleranceContextCircuitOnly(purchaseRequestDTO,stockResponseList);
					redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerCustomerClient");
						return response;
				} catch (Exception e) {
						return fallBackMethod(purchaseRequestDTO, stockResponseList, e);
				}
			}
	
			case "OPEN" -> {
				return fallBackMethod(purchaseRequestDTO, stockResponseList, null);
			}
	
			default -> throw new CircuitBreakerCustomException("Save Step - Status desconhecido: " + currentState);
			}
	}

	private PurchaseHistoryResponseDTO saveHistoryFaultToleranceContextCircuitOnly(PurchaseRequestDTO purchaseRequestDTO, 
			List<StockResponseDTO> stockResponseList) throws Exception {
		
		PurchaseHistoryResponseDTO response = CircuitBreaker.decorateCallable(circuitBreakerCustomerClient, () -> {
			ResponseEntity<PurchaseHistoryResponseDTO>  responseCustomerClient = customerClient.
					persistPurchaseHistory(Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseList));
		        		return responseCustomerClient.getBody();
		}).call();
		return response;
	}
	
	private PurchaseHistoryResponseDTO saveHistoryFaultToleranceContext(PurchaseRequestDTO purchaseRequestDTO,List<StockResponseDTO> stockResponseList)
				throws Exception {
		
		return Retry.decorateCallable(retryCustomerClient, circuitBreakerCustomerClient.decorateCallable(() -> {
			ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(
					Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseList));
			return responseCustomerClient.getBody();
		})).call();
	}
	
	public PurchaseHistoryResponseDTO fallBackMethod(PurchaseRequestDTO dto,List<StockResponseDTO> stockResponseList, Throwable e)
			throws JsonProcessingException {
		
		redisCircuitBreakerManager.updateRequestResultsInRedis("0","circuitBreakerCustomerClient");
		String circuitBreakerState = circuitBreakerCustomerClient.getState().name();
	
		circuitBreakerStrategyContext.setStrategy(circuitBreakerState + "_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(dto, stockResponseList, e);
			
		throw new CircuitBreakerCustomException("Falha no fallback Save History step: "+e.getMessage());
	}

}
