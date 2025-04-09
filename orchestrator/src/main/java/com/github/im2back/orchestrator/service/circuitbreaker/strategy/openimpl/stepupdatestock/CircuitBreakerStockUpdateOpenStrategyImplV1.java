package com.github.im2back.orchestrator.service.circuitbreaker.strategy.openimpl.stepupdatestock;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessUpdateStock;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyInterface;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CircuitBreakerStockUpdateOpenStrategyImplV1 implements CircuitBreakerStrategyInterface {
	
	private final PublishReprocessUpdateStock publishReprocessUpdateStock;

	@Override
	public void execute(PurchaseRequestDTO purchaseRequestDTO , List<StockResponseDTO> stockUpdateResponseDTOList,
			Throwable e) throws JsonProcessingException {
		
		//Enviar para a fila para atualização de estoque assincrona 
		//Atualiza o estoque e envia menssagem para a fila do customer processar de forma assincrona
		publishReprocessUpdateStock.sendReprocessHistory(purchaseRequestDTO);		
		
		//Lança a excecao e breka o processamento da etapa 3 qe será realizada de forma assincrona
		throw new AsynchronousProcessingException("Pedido enviado para processamento assincrono.",202);
		
	}

}
