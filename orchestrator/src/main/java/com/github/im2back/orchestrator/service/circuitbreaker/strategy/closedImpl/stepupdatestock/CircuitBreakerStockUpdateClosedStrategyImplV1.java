package com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedImpl.stepupdatestock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyInterface;

@Service
public class CircuitBreakerStockUpdateClosedStrategyImplV1 implements CircuitBreakerStrategyInterface {

	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO, List<StockUpdateResponseDTO> stockUpdateResponseDTOList,
			Throwable e) throws JsonProcessingException,ServiceUnavailableCustomException {	

		System.out.println();
		System.out.println("CLOSED - UpdateStock ");
		System.out.println();
		
		throw new ServiceUnavailableCustomException("Compra cancelada. Causa: "+e.getMessage(), 503, null);	
		
	}

}
