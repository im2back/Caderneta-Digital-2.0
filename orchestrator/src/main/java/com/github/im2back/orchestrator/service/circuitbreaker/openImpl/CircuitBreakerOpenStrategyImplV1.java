package com.github.im2back.orchestrator.service.circuitbreaker.openImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerInterfaceStrategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerOpenStrategyImplV1 implements CircuitBreakerInterfaceStrategy {
	
	private final StockClient stockClient;
	
	@Override
	public void execute(List<StockUpdateResponseDTO> stockUpdateResponseDTOList,Throwable e) throws ServiceUnavailableCustomException{
		
		System.out.println("(=======> CIRCUITO ABERTO - ENVIANDO PARA REPROCESSAMENTO <======)");
						
	}

}
