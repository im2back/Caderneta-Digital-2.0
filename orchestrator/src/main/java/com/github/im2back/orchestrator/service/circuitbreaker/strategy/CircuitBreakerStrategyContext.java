package com.github.im2back.orchestrator.service.circuitbreaker.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedImpl.stepsavehistory.CircuitBreakerSaveHistoryClosedStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedImpl.stepupdatestock.CircuitBreakerStockUpdateClosedStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenImpl.stepsavehistory.CircuitBreakerSaveHistoryHalfOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenImpl.stepupdatestock.CircuitBreakerUpdateStockHalfOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.openImpl.stepsavehistory.CircuitBreakerSaveHistoryOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.openImpl.stepupdatestock.CircuitBreakerStockUpdateOpenStrategyImplV1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CircuitBreakerStrategyContext {

	private CircuitBreakerStrategyInterface circuitBreakerStateStrategy;
	
	private final CircuitBreakerSaveHistoryClosedStrategyImplV1 circuitBreakerSaveHistoryClosedStrategyImplV1;
	private final CircuitBreakerSaveHistoryOpenStrategyImplV1 circuitBreakerSaveHistoryOpenStrategyImplV1;
	private final CircuitBreakerSaveHistoryHalfOpenStrategyImplV1 circuitBreakerSaveHistoryHalfOpenStrategyImplV1;
	
	private final CircuitBreakerStockUpdateClosedStrategyImplV1 circuitBreakerStockUpdateClosedStrategyImplV1;
	private final CircuitBreakerStockUpdateOpenStrategyImplV1 circuitBreakerStockUpdateOpenStrategyImplV1;
	private final CircuitBreakerUpdateStockHalfOpenStrategyImplV1 circuitBreakerUpdateStockHalfOpenStrategyImplV1;


	private static final Map<String, CircuitBreakerStrategyInterface> strategies = new HashMap<>();

	@PostConstruct
	private void initializeStrategies() {
		strategies.put("OPEN_CIRCUITBREAKERCUSTOMERCLIENT", circuitBreakerSaveHistoryOpenStrategyImplV1);
		strategies.put("CLOSED_CIRCUITBREAKERCUSTOMERCLIENT", circuitBreakerSaveHistoryClosedStrategyImplV1);
		strategies.put("HALF_OPEN_CIRCUITBREAKERCUSTOMERCLIENT", circuitBreakerSaveHistoryHalfOpenStrategyImplV1);

		strategies.put("OPEN_CIRCUITBREAKERSTOCKCLIENT", circuitBreakerStockUpdateOpenStrategyImplV1);
		strategies.put("CLOSED_CIRCUITBREAKERSTOCKCLIENT", circuitBreakerStockUpdateClosedStrategyImplV1);
		strategies.put("HALF_OPEN_CIRCUITBREAKERSTOCKCLIENT", circuitBreakerUpdateStockHalfOpenStrategyImplV1);
		
	}

	public void setStrategy(String param) {
		this.circuitBreakerStateStrategy = strategies.getOrDefault(param, null);
	}

	public void executeStrategy(PurchaseRequestDTO purchaseRequestDTO,
			List<StockResponseDTO> stockUpdateResponseDTOList, Throwable e) throws JsonProcessingException {
		circuitBreakerStateStrategy.execute(purchaseRequestDTO, stockUpdateResponseDTOList, e);
	}
}
