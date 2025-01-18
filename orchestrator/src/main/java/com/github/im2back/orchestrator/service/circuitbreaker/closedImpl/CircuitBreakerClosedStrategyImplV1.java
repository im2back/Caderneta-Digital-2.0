package com.github.im2back.orchestrator.service.circuitbreaker.closedImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerInterfaceStrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerClosedStrategyImplV1 implements CircuitBreakerInterfaceStrategy {
	
	private final StockClient stockClient;
	
	@Override
	public void execute(List<StockUpdateResponseDTO> stockUpdateResponseDTOList,Throwable e) {
		
		System.out.println("(=======> CLOSED <======)");
	
		stockClient.massiveReplenishmentInStock(stockUpdateResponseDTOList);			
		throw new ServiceUnavailableCustomException("Compra cancelada. Causa: "+e.getMessage(), 503, null);		
	}

}
