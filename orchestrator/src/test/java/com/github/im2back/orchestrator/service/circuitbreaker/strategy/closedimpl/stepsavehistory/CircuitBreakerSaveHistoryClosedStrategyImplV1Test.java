package com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedimpl.stepsavehistory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.MassiveReplenishmentResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerSaveHistoryClosedStrategyImplV1Test {
	
	@InjectMocks
	private CircuitBreakerSaveHistoryClosedStrategyImplV1 breakerSaveHistoryClosedStrategyImplV1;
	
	@Mock
	private StockClient stockClient;
	
	@Test
	@DisplayName("Shoud throw ServiceUnavailableCustomException after acionar the stockClient ")
	void test() {
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
		
		ResponseEntity<List<MassiveReplenishmentResponseDTO>> mockResponseEntity =
			    new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		BDDMockito.when(stockClient.massiveReplenishmentInStock(responseDTOs)).thenReturn(mockResponseEntity);
	
		
		//ACT + ASSERT
		assertThrows(ServiceUnavailableCustomException.class, 
				()-> breakerSaveHistoryClosedStrategyImplV1.execute(purchaseRequestDTO, responseDTOs, throwable));
		
		verify(stockClient).massiveReplenishmentInStock(responseDTOs);
	}

}
