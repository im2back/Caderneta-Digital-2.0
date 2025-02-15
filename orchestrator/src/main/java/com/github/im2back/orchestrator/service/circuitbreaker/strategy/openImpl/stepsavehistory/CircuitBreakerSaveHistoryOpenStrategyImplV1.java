package com.github.im2back.orchestrator.service.circuitbreaker.strategy.openImpl.stepsavehistory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessSaveHistory;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyInterface;
import com.github.im2back.orchestrator.service.utils.Utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerSaveHistoryOpenStrategyImplV1 implements CircuitBreakerStrategyInterface {
		
	private final PublishReprocessSaveHistory publishReprocessHistory;
	
	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO,List<StockUpdateResponseDTO> stockUpdateResponseDTOList,Throwable e)
			throws ServiceUnavailableCustomException, JsonProcessingException {	
		System.out.println();
		System.out.println("OPEN - SaveHistory Async ");
		System.out.println();
		
		var purchaseHistoryDTO = Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockUpdateResponseDTOList);
		
		publishReprocessHistory.sendReprocessHistory(purchaseHistoryDTO);
		
		throw new AsynchronousProcessingException("Pedido enviado para processamento assincrono.",202);
	}
	
	
	

}
