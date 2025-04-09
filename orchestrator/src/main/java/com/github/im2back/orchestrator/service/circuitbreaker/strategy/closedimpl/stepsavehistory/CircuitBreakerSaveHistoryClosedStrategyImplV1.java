package com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedimpl.stepsavehistory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyInterface;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerSaveHistoryClosedStrategyImplV1 implements CircuitBreakerStrategyInterface {
	
	private final StockClient stockClient;
	
	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO,List<StockResponseDTO> stockUpdateResponseDTOList,Throwable e) {
		stockClient.massiveReplenishmentInStock(stockUpdateResponseDTOList);			
		throw new ServiceUnavailableCustomException("Compra cancelada. Causa: "+e.getMessage(), 503, null);		
	}

}
