package com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenImpl.stepsavehistory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.amqp.publishers.PublishReprocessSaveHistory;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.utils.Utils;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerSaveHistoryHalfOpenStrategyImplV1Test {

	@InjectMocks
	private CircuitBreakerSaveHistoryHalfOpenStrategyImplV1 circuitBreakerSaveHistoryHalfOpenStrategyImplV1;

	@Mock
	private PublishReprocessSaveHistory publishReprocessHistory;

	@Test
	@DisplayName("Should throw AsynchronousProcessingException when an asynchronous processing error occurs")
	void shouldThrowAsynchronousProcessingExceptionWhenProcessingFails() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockResponseDTO = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
		PurchaseHistoryDTO purchaseHistoryDTO = Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseDTO);

		try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {
			mockedUtils.when(() -> Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseDTO))
					.thenReturn(purchaseHistoryDTO);

			doNothing().when(publishReprocessHistory).sendReprocessHistory(purchaseHistoryDTO);

			// ACT + ASSERT
			assertThrows(AsynchronousProcessingException.class, () -> circuitBreakerSaveHistoryHalfOpenStrategyImplV1
					.execute(purchaseRequestDTO, stockResponseDTO, throwable));

			verify(publishReprocessHistory).sendReprocessHistory(purchaseHistoryDTO);
			mockedUtils.verify(() -> Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseDTO));
		}
	}

}
