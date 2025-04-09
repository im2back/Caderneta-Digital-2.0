package com.github.im2back.orchestrator.service.circuitbreaker.strategy.openimpl.stepsavehistory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessSaveHistory;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyInterface;
import com.github.im2back.orchestrator.service.utils.Utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerSaveHistoryOpenStrategyImplV1 implements CircuitBreakerStrategyInterface {
		
	private final PublishReprocessSaveHistory publishReprocessHistory;
	
	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO,List<StockResponseDTO> stockUpdateResponseDTOList,Throwable e)
			throws ServiceUnavailableCustomException, JsonProcessingException {	

		PurchaseHistoryDTO purchaseHistoryDTO = Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockUpdateResponseDTOList);
		
		publishReprocessHistory.sendReprocessHistory(purchaseHistoryDTO);
		
		throw new AsynchronousProcessingException("Pedido enviado para processamento assincrono.",202);
	}
	
	
	

}
