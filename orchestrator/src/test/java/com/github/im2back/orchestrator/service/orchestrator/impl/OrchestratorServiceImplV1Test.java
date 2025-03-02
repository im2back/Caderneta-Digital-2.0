package com.github.im2back.orchestrator.service.orchestrator.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.exception.FeignClientCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.service.steps.savehistorystep.SaveHistoryStep;
import com.github.im2back.orchestrator.service.steps.updatestockstep.UpdateStockStep;
import com.github.im2back.orchestrator.service.steps.validationstep.ValidationStep;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class OrchestratorServiceImplV1Test {
	
	@InjectMocks
	private OrchestratorServiceImplV1 orchestratorServiceV1;
	
	@Mock
	private ValidationStep validationStep;
	@Mock
	private UpdateStockStep updateStockStep;
	@Mock
	private SaveHistoryStep saveHistoryStep;
	
	@Captor
	private ArgumentCaptor<PurchaseRequestDTO> validationCaptor;
	@Captor
	private ArgumentCaptor<PurchaseRequestDTO> stockStepCaptor;
	@Captor
	private ArgumentCaptor<PurchaseRequestDTO> saveHistoryCaptor;
	@Captor
	private ArgumentCaptor<List<StockResponseDTO>> stockUpdateResponseDTOCaptor;
	
	@Test
	@DisplayName("Should invoke all steps and return a PurchaseHistoryResponseDTO when no business validation error occurs")
	void orchestratePurchaseShouldInvokeAllStepsAndReturnPurchaseHistoryResponseDTOWhenNoBusinessValidationErrorOccurs() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		
		BDDMockito.doNothing().when(validationStep).execute(purchaseRequestDTO);
		BDDMockito.when(updateStockStep.execute(purchaseRequestDTO)).thenReturn(PurchaseTestFactory.createSuccessfulStockUpdateResponse());
		BDDMockito.when(saveHistoryStep.execute(purchaseRequestDTO, PurchaseTestFactory.createSuccessfulStockUpdateResponse()))
		          .thenReturn(PurchaseTestFactory.createSuccessfulPurchaseHistory());
		
		// ACT 
		PurchaseHistoryResponseDTO response = orchestratorServiceV1.orchestratePurchase(purchaseRequestDTO);
		
		// ASSERT
		BDDMockito.then(validationStep).should().execute(validationCaptor.capture());
		BDDMockito.then(updateStockStep).should().execute(stockStepCaptor.capture());
		BDDMockito.then(saveHistoryStep).should().execute(saveHistoryCaptor.capture(), stockUpdateResponseDTOCaptor.capture());
		
		verify(validationStep).execute(any());
		verify(updateStockStep).execute(any());
		verify(saveHistoryStep).execute(any(), any());
		
		Assertions.assertEquals(purchaseRequestDTO, validationCaptor.getValue(), 
		    "The argument passed to the Validation module should match the parameter received by orchestratePurchase");
		Assertions.assertEquals(purchaseRequestDTO, stockStepCaptor.getValue(), 
		    "The argument passed to the Stock module should match the parameter received by orchestratePurchase");
		Assertions.assertEquals(purchaseRequestDTO, saveHistoryCaptor.getValue(), 
		    "The argument passed to the Customer module should match the parameter received by orchestratePurchase");
		Assertions.assertEquals(PurchaseTestFactory.createSuccessfulPurchaseHistory(), response, 
		    "The main method should return the Purchase DTO provided by the Customer module");
	}
	
	@Test
	@DisplayName("Should throw a FeignClientCustomException when ValidationStep fails")
	void orchestratePurchaseShouldThrowFeignClientCustomExceptionWhenValidationStepFails() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		
		BDDMockito.willThrow(ExceptionTestFactory.createFeignClientCustomExeptionState422())
		          .given(validationStep).execute(purchaseRequestDTO);
		
		// ACT + ASSERT	
		Assertions.assertThrows(FeignClientCustomException.class, () -> 
		    orchestratorServiceV1.orchestratePurchase(purchaseRequestDTO)
		);
	}
	
	@Test
	@DisplayName("Should throw a RuntimeException when StockStep fails")
	void orchestratePurchaseShouldThrowRuntimeExceptionWhenStockStepFails() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		
		BDDMockito.willThrow(ExceptionTestFactory.createRuntimeException())
		          .given(updateStockStep).execute(purchaseRequestDTO);
		
		// ACT + ASSERT	
		Assertions.assertThrows(RuntimeException.class, () -> 
		    orchestratorServiceV1.orchestratePurchase(purchaseRequestDTO),
		    "Should cut the flow and throw an exception if StockStep fails"
		);
	}
	
	@Test
	@DisplayName("Should throw a RuntimeException when SaveHistoryStep fails)")
	void orchestratePurchaseShouldThrowRuntimeExceptionWhenSaveHistoryStepFail() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockUpdateResponseDTO = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		
		BDDMockito.willThrow(ExceptionTestFactory.createRuntimeException())
		          .given(saveHistoryStep).execute(purchaseRequestDTO, stockUpdateResponseDTO);
		
		// ACT + ASSERT	
		Assertions.assertThrows(RuntimeException.class, () -> 
		    orchestratorServiceV1.orchestratePurchase(purchaseRequestDTO),
		    "Should cut the flow and throw an exception if SaveHistoryStep fails (Scenario 1)"
		);
	}
	
	@Test
	@DisplayName("Should throw a RuntimeException when SaveHistoryStep fails")
	void orchestratePurchaseShouldThrowRuntimeExceptionWhenSaveHistoryStepFails() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockUpdateResponseDTO = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		
		BDDMockito.willThrow(ExceptionTestFactory.createRuntimeException())
		          .given(saveHistoryStep).execute(purchaseRequestDTO, stockUpdateResponseDTO);
		
		// ACT + ASSERT	
		Assertions.assertThrows(RuntimeException.class, () -> 
		    orchestratorServiceV1.orchestratePurchase(purchaseRequestDTO),
		    "Should cut the flow and throw an exception if SaveHistoryStep fails (Scenario 2)"
		);
	}
}
