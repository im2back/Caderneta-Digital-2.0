package com.github.im2back.orchestrator.service.circuitbreaker.halfopenImpl.stepsavehistory;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessSaveHistory;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.circuitbreaker.CircuitBreakerStrategyInterface;
import com.github.im2back.orchestrator.service.utils.ServiceUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CircuitBreakerSaveHistoryHalfOpenStrategyImplV1 implements CircuitBreakerStrategyInterface {

	private final PublishReprocessSaveHistory publishReprocessHistory;

	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO, List<StockUpdateResponseDTO> stockUpdateResponseDTOList,
			Throwable e) throws JsonProcessingException {

		System.out.println();
		System.out.println("====>ENTROU NO HALF OPEN<====");
		System.out.println();

		var purchaseHistoryDTO = ServiceUtils.assemblePurchaseHistoryDTO(purchaseRequestDTO,
				stockUpdateResponseDTOList);

		publishReprocessHistory.sendReprocessHistory(purchaseHistoryDTO);

		throw new AsynchronousProcessingException("Pedido enviado para processamento assincrono.", 202);
	}

}
