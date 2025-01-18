package com.github.im2back.orchestrator.service.circuitbreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.closedImpl.CircuitBreakerClosedStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.openImpl.CircuitBreakerOpenStrategyImplV1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CircuitBreakerStrategyContext {

	private CircuitBreakerInterfaceStrategy circuitBreakerStateStrategy;
	private final CircuitBreakerClosedStrategyImplV1 circuitBreakerClosedStrategyV1;
	private final CircuitBreakerOpenStrategyImplV1 circuitBreakerOpenStrategyV1;
	
	private static final Map<String,CircuitBreakerInterfaceStrategy> strategies = new HashMap<>();
	
	@PostConstruct
	private void initializeStrategies() {
		strategies.put("OPEN", circuitBreakerOpenStrategyV1);
		strategies.put("CLOSED", circuitBreakerClosedStrategyV1);		
	}
	
	public void setStrategy(String param) {
		this.circuitBreakerStateStrategy = strategies.getOrDefault(param,null);
	}
	
	public void executeStrategy(List<StockUpdateResponseDTO> stockUpdateResponseDTOList,Throwable e) {
		circuitBreakerStateStrategy.execute(stockUpdateResponseDTOList,e);
	}
}
