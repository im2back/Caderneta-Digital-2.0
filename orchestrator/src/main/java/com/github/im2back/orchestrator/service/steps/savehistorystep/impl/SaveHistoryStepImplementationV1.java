package com.github.im2back.orchestrator.service.steps.savehistorystep.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.CustomerClient;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;
import com.github.im2back.orchestrator.service.utils.ServiceUtils;
import com.github.im2back.orchestrator.utils.Util;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class SaveHistoryStepImplementationV1 implements SaveHistoryStep {

	private final CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	private final CustomerClient customerClient;
	private final CircuitBreakerRegistry circuitBreakerRegistry;
	private final RetryRegistry retryRegistry;

	@Override
	public PurchaseHistoryResponseDTO execute(PurchaseRequestDTO purchaseRequestDTO,
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList) throws JsonProcessingException {

		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreakerCustomerClient");
		Retry retry = retryRegistry.retry("retryCustomerClient");

		String circuitBreakerState = Util.getCircuitBreakerState(circuitBreakerRegistry,"circuitBreakerCustomerClient");

		switch (circuitBreakerState) {
		case "CLOSED", "HALF_OPEN" -> {
			try {
				return executeWithRetryAndCircuitBreakerContext(purchaseRequestDTO, stockUpdateResponseDTOList,circuitBreaker, retry);
			} catch (Exception e) {
				return fallbackMethod(purchaseRequestDTO, stockUpdateResponseDTOList, e);
			}
		}

		case "OPEN" -> {
			return fallbackMethod(purchaseRequestDTO, stockUpdateResponseDTOList, null);
		}
		default -> throw new CircuitBreakerCustomException("Status desconhecido: " + circuitBreakerState);
		}
	}

	public PurchaseHistoryResponseDTO fallbackMethod(PurchaseRequestDTO dto,
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList, Throwable e) throws JsonProcessingException {
		String circuitBreakerState = Util.getCircuitBreakerState(circuitBreakerRegistry,
				"circuitBreakerCustomerClient");

		circuitBreakerStrategyContext.setStrategy(circuitBreakerState + "_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(dto, stockUpdateResponseDTOList, e);
		return null; // retorno nulo apenas para compilar
	}

	private PurchaseHistoryResponseDTO executeWithRetryAndCircuitBreakerContext(PurchaseRequestDTO purchaseRequestDTO,
			List<StockUpdateResponseDTO> stockUpdateResponseDTOList, CircuitBreaker circuitBreaker, Retry retry)
			throws Exception {
		return Retry.decorateCallable(retry, circuitBreaker.decorateCallable(() -> {
			ResponseEntity<PurchaseHistoryResponseDTO> responseCustomerClient = customerClient.persistPurchaseHistory(
					ServiceUtils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockUpdateResponseDTOList));
			return responseCustomerClient.getBody();
		})).call();
	}

}
