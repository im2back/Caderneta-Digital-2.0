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
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
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
	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO purchaseRequestDTO,List<StockUpdateResponseDTO> stockUpdateResponseDTOList) 
			throws JsonProcessingException {
	
		String currentState = redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient,"circuitBreakerCustomerClient");

		switch (currentState) {
			case "CLOSED" -> {
				redisCircuitBreakerManager.closedEvaluateCircuitTransition("circuitBreakerCustomerClient");
				try {	
					 var response = saveHistoryFaultToleranceContext(purchaseRequestDTO, stockUpdateResponseDTOList,circuitBreakerCustomerClient, retryCustomerClient);
					 redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerCustomerClient");
						return response;					
			   } catch (Exception e) {
						return fallbackMethod(purchaseRequestDTO, stockUpdateResponseDTOList, e);
				}
			}
			
			case "HALF_OPEN" -> {
				redisCircuitBreakerManager.halfOpenEvaluateCircuitTransition("circuitBreakerCustomerClient");		
				try {
					var response = saveHistoryFaultToleranceContextCircuitOnly(purchaseRequestDTO,stockUpdateResponseDTOList, circuitBreakerCustomerClient);
					redisCircuitBreakerManager.updateRequestResultsInRedis("1","circuitBreakerCustomerClient");
						return response;
				} catch (Exception e) {
						return fallbackMethod(purchaseRequestDTO, stockUpdateResponseDTOList, e);
				}
			}
	
			case "OPEN" -> {
				return fallbackMethod(purchaseRequestDTO, stockUpdateResponseDTOList, null);
			}
			
			default -> throw new CircuitBreakerCustomException("Save Step - Status desconhecido: " + currentState);
			}
	}

	private PurchaseHistoryResponseDTO saveHistoryFaultToleranceContextCircuitOnly(PurchaseRequestDTO purchaseRequestDTO, 
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList, CircuitBreaker circuitBreaker) throws Exception {
		var response = CircuitBreaker.decorateCallable(circuitBreaker, () -> {
		var responseCustomerClient = customerClient.persistPurchaseHistory(Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockUpdateResponseDTOList));
		        return responseCustomerClient.getBody();
		    }).call();
		return response;
	}
	
	private PurchaseHistoryResponseDTO saveHistoryFaultToleranceContext(PurchaseRequestDTO purchaseRequestDTO,
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList, CircuitBreaker circuitBreaker, Retry retry)
			throws Exception {
		return Retry.decorateCallable(retry, circuitBreaker.decorateCallable(() -> {
			ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(
					Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockUpdateResponseDTOList));
			return responseCustomerClient.getBody();
		})).call();
	}
	
	public PurchaseHistoryResponseDTO fallbackMethod(PurchaseRequestDTO dto,List<StockUpdateResponseDTO> stockUpdateResponseDTOList, Throwable e)
			throws JsonProcessingException {
		
		redisCircuitBreakerManager.updateRequestResultsInRedis("0","circuitBreakerCustomerClient");
		CircuitBreaker circuitBreaker = redisCircuitBreakerManager.getCircuitBreaker("circuitBreakerCustomerClient");
		String circuitBreakerState = circuitBreaker.getState().name();
	
		circuitBreakerStrategyContext.setStrategy(circuitBreakerState + "_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(dto, stockUpdateResponseDTOList, e);
			
		throw new CircuitBreakerCustomException("Falha no fallback Save History step: "+e.getMessage());
	}

}
