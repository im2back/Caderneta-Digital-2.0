package com.github.im2back.orchestrator.service.circuitbreaker.strategy;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedimpl.stepsavehistory.CircuitBreakerSaveHistoryClosedStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.closedimpl.stepupdatestock.CircuitBreakerStockUpdateClosedStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenimpl.stepsavehistory.CircuitBreakerSaveHistoryHalfOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.halfopenimpl.stepupdatestock.CircuitBreakerUpdateStockHalfOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.openimpl.stepsavehistory.CircuitBreakerSaveHistoryOpenStrategyImplV1;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.openimpl.stepupdatestock.CircuitBreakerStockUpdateOpenStrategyImplV1;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerStrategyContextTest {

	private CircuitBreakerStrategyContext circuitBreakerStrategyContext;

	@Mock
	private CircuitBreakerStrategyInterface circuitBreakerStateStrategy;

	@Mock
	private CircuitBreakerSaveHistoryClosedStrategyImplV1 circuitBreakerSaveHistoryClosedStrategyImplV1;

	@Mock
	private CircuitBreakerSaveHistoryOpenStrategyImplV1 circuitBreakerSaveHistoryOpenStrategyImplV1;

	@Mock
	private CircuitBreakerSaveHistoryHalfOpenStrategyImplV1 circuitBreakerSaveHistoryHalfOpenStrategyImplV1;

	@Mock
	private CircuitBreakerStockUpdateClosedStrategyImplV1 circuitBreakerStockUpdateClosedStrategyImplV1;

	@Mock
	private CircuitBreakerStockUpdateOpenStrategyImplV1 circuitBreakerStockUpdateOpenStrategyImplV1;

	@Mock
	private CircuitBreakerUpdateStockHalfOpenStrategyImplV1 circuitBreakerUpdateStockHalfOpenStrategyImplV1;

	@BeforeEach
	void setup() throws Exception {
		MockitoAnnotations.openMocks(this);
		this.circuitBreakerStrategyContext = new CircuitBreakerStrategyContext(
				circuitBreakerSaveHistoryClosedStrategyImplV1, circuitBreakerSaveHistoryOpenStrategyImplV1,
				circuitBreakerSaveHistoryHalfOpenStrategyImplV1, circuitBreakerStockUpdateClosedStrategyImplV1,
				circuitBreakerStockUpdateOpenStrategyImplV1, circuitBreakerUpdateStockHalfOpenStrategyImplV1);

		Method initMethod = CircuitBreakerStrategyContext.class.getDeclaredMethod("initializeStrategies");
		initMethod.setAccessible(true);
		initMethod.invoke(circuitBreakerStrategyContext);
	}

	@Test
	@DisplayName("Should correctly execute strategy when OPEN_CIRCUITBREAKERCUSTOMERCLIENT is set")
	void shouldExecuteStrategyWhenOpenCircuitBreakerCustomerClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerSaveHistoryOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("OPEN_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerSaveHistoryOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

	@Test
	@DisplayName("Should correctly execute strategy when CLOSED_CIRCUITBREAKERCUSTOMERCLIENT is set")
	void shouldExecuteStrategyWhenClosedCircuitBreakerCustomerClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerSaveHistoryClosedStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("CLOSED_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerSaveHistoryClosedStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

	@Test
	@DisplayName("Should correctly execute strategy when HALF_OPEN_CIRCUITBREAKERCUSTOMERCLIENT is set")
	void shouldExecuteStrategyWhenHalfOpenCircuitBreakerCustomerClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerSaveHistoryHalfOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("HALF_OPEN_CIRCUITBREAKERCUSTOMERCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerSaveHistoryHalfOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

	@Test
	@DisplayName("Should correctly execute strategy when OPEN_CIRCUITBREAKERSTOCKCLIENT is set")
	void shouldExecuteStrategyWhenOpenCircuitBreakerStockClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerStockUpdateOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("OPEN_CIRCUITBREAKERSTOCKCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerStockUpdateOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

	@Test
	@DisplayName("Should correctly execute strategy when CLOSED_CIRCUITBREAKERSTOCKCLIENT is set")
	void shouldExecuteStrategyWhenClosedCircuitBreakerStockClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerStockUpdateClosedStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("CLOSED_CIRCUITBREAKERSTOCKCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerStockUpdateClosedStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

	@Test
	@DisplayName("Should correctly execute strategy when HALF_OPEN_CIRCUITBREAKERSTOCKCLIENT is set")
	void shouldExecuteStrategyWhenHalfOpenCircuitBreakerStockClientIsSet() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> responseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();

		doNothing().when(circuitBreakerUpdateStockHalfOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs,
				throwable);

		// ACT
		circuitBreakerStrategyContext.setStrategy("HALF_OPEN_CIRCUITBREAKERSTOCKCLIENT");
		circuitBreakerStrategyContext.executeStrategy(purchaseRequestDTO, responseDTOs, throwable);

		// ASSERT
		verify(circuitBreakerUpdateStockHalfOpenStrategyImplV1).execute(purchaseRequestDTO, responseDTOs, throwable);
	}

}
