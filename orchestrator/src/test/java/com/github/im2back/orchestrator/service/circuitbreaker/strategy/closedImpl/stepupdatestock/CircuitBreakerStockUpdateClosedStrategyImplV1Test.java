package com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedImpl.stepupdatestock;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

class CircuitBreakerStockUpdateClosedStrategyImplV1Test {
	
	private CircuitBreakerStockUpdateClosedStrategyImplV1 breakerStockUpdateClosedStrategyImplV1;
	
	  @BeforeEach
	    void setUp() {
	        breakerStockUpdateClosedStrategyImplV1 = new CircuitBreakerStockUpdateClosedStrategyImplV1(); 
	    }
	
	  @Test
	  @DisplayName("Should throw ServiceUnavailableCustomException when stock update fails")
	  void shouldThrowServiceUnavailableCustomExceptionWhenStockUpdateFails() {
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		//ACT + ASSERT
		assertThrows(ServiceUnavailableCustomException.class, 
				() -> breakerStockUpdateClosedStrategyImplV1.execute(purchaseRequestDTO, responseDTOs, throwable));
	}
}

