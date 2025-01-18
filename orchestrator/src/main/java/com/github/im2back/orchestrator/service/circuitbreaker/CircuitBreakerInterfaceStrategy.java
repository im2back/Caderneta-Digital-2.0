package com.github.im2back.orchestrator.service.circuitbreaker;

import java.util.List;

import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;

public interface CircuitBreakerInterfaceStrategy {
	
	void execute(List<StockUpdateResponseDTO> stockUpdateResponseDTOList,Throwable e);

}
