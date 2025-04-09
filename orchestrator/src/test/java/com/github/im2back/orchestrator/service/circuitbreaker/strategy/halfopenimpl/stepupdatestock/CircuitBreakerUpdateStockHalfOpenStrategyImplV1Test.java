package com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenimpl.stepupdatestock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessUpdateStock;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerUpdateStockHalfOpenStrategyImplV1Test {
	
	@InjectMocks
	private CircuitBreakerUpdateStockHalfOpenStrategyImplV1 circuitBreakerUpdateStockHalfOpenStrategyImplV1;
	@Mock
	private  PublishReprocessUpdateStock publishReprocessUpdateStock;
	
	@Test
	@DisplayName("Should throw AsynchronousProcessingException and trigger reprocess when stock update fails")
	void shouldThrowAsynchronousProcessingExceptionAndTriggerReprocessWhenStockUpdateFails() throws JsonProcessingException {
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockResponseDTO = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
		doNothing().when(publishReprocessUpdateStock).sendReprocessHistory(any());
		
		 //ACT
		assertThrows(AsynchronousProcessingException.class, 
				() ->circuitBreakerUpdateStockHalfOpenStrategyImplV1.execute(purchaseRequestDTO, stockResponseDTO, throwable));
		
		//ASSERT
		verify(publishReprocessUpdateStock).sendReprocessHistory(purchaseRequestDTO);
		
	}

}
