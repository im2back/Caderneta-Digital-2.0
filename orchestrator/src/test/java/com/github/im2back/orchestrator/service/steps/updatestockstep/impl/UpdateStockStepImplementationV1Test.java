package com.github.im2back.orchestrator.service.steps.updatestockstep.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.im2back.orchestrator.clients.StockClient;
import com.github.im2back.orchestrator.clients.exception.ServiceUnavailableCustomException;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.exception.customexceptions.AsynchronousProcessingException;
import com.github.im2back.orchestrator.service.circuitbreaker.manager.RedisCircuitBreakerStateManager;
import com.github.im2back.orchestrator.service.circuitbreaker.strategy.CircuitBreakerStrategyContext;
import com.github.im2back.orchestrator.util.ExceptionTestFactory;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;

@ExtendWith(MockitoExtension.class)
class UpdateStockStepImplementationV1Test {
	@InjectMocks
	private UpdateStockStepImplementationV1 stockStep;
	
	@Mock
	private StockClient stockClient;
	@Mock
	private CircuitBreakerStrategyContext circuitBreakerStrategyContext;
	@Mock
	private RedisCircuitBreakerStateManager redisCircuitBreakerManager;
	@Mock
	private CircuitBreaker circuitBreakerStockClient;
	
	private MockedStatic<Retry> mockedRetry; 
	
	@BeforeEach
    void setup() {
        lenient().when(circuitBreakerStockClient.decorateCallable(any()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        mockedRetry = mockStatic(Retry.class);
        mockedRetry.when(() -> Retry.decorateCallable(any(), any()))
            .thenAnswer(invocation -> invocation.getArgument(1));
    }

    @AfterEach
    void tearDown() {
        if (mockedRetry != null) {
            mockedRetry.close();
        }
    }


    @Test
    @DisplayName("Should return a PurchaseHistoryResponseDTO when the CircuitBreaker is CLOSED")
    void executeShouldReturnPurchaseHistoryResponseDTOWhenCircuitBreakerIsClosed() throws JsonProcessingException {

		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockUpdateResponseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		String circuitBreakerName = "circuitBreakerStockClient";
		String state ="CLOSED";
		String resultRequest = "1";
		ResponseEntity<List<StockResponseDTO>> responseStock = new ResponseEntity<>(stockUpdateResponseDTOs,HttpStatus.OK);
		
		
		BDDMockito.when(redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient, circuitBreakerName)).thenReturn(state);
		BDDMockito.when(stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems())).thenReturn(responseStock);
		doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		doNothing().when(redisCircuitBreakerManager).closedEvaluateCircuitTransition(circuitBreakerName);
		
		//ACT
		var response = stockStep.execute(purchaseRequestDTO);
		
		//ASSERT
		verify(redisCircuitBreakerManager).closedEvaluateCircuitTransition(circuitBreakerName);
		verify(redisCircuitBreakerManager).updateLocalCircuitBreakerState(any(), any());
		verify(redisCircuitBreakerManager).updateRequestResultsInRedis("1", circuitBreakerName);
		verify(stockClient).updateStockAfterPurchase(purchaseRequestDTO.purchasedItems());
		Assertions.assertEquals(stockUpdateResponseDTOs, response, "Deveria retornar a resposta devolvida pelo microsserviço de Stock");
		
	}
    
    @Test
    @DisplayName("Should throw a ServiceUnavailableCustomException when the fallback is triggered and the CircuitBreaker is CLOSED")
    void executeShouldThrowServiceUnavailableCustomExceptionWhenFallbackIsTriggeredAndCircuitBreakerIsClosed() throws JsonProcessingException {

		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		String circuitBreakerName = "circuitBreakerStockClient";
		String state ="CLOSED";
		String resultRequest = "0";	
		
		BDDMockito.when(redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient, circuitBreakerName)).thenReturn(state);
		BDDMockito.when(stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems()))
			.thenThrow(ExceptionTestFactory.createServiceUnavailableCustomException());	
		
		BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.CLOSED);

		doThrow(ExceptionTestFactory.createServiceUnavailableCustomException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any(Throwable.class));
		
		doNothing().when(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		doNothing().when(redisCircuitBreakerManager).closedEvaluateCircuitTransition(circuitBreakerName);
		
		//ACT+ASSERT
		assertThrows(ServiceUnavailableCustomException.class, () -> stockStep.execute(purchaseRequestDTO));
		
		verify(redisCircuitBreakerManager).updateLocalCircuitBreakerState(any(), any());
		verify(redisCircuitBreakerManager).closedEvaluateCircuitTransition(circuitBreakerName);
		verify(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		verify(circuitBreakerStockClient).getState();
		verify(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any(Throwable.class));		
	}
    
    @Test
    @DisplayName("Should throw an AsynchronousProcessingException when the fallback is triggered and the CircuitBreaker is OPEN")
    void executeShouldThrowAsynchronousProcessingExceptionWhenFallbackIsTriggeredAndCircuitBreakerIsOpen() throws JsonProcessingException {

		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		String circuitBreakerName = "circuitBreakerStockClient";
		String state ="OPEN";
		String resultRequest = "0";	
		
		BDDMockito.when(redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient, circuitBreakerName)).thenReturn(state);	
		BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.OPEN);

		doThrow(ExceptionTestFactory.createAsynchronousProcessingException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO),any(),any());
		
		doNothing().when(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		
		//ACT+ASSERT
		assertThrows(AsynchronousProcessingException.class, () -> stockStep.execute(purchaseRequestDTO));
		
		verify(redisCircuitBreakerManager).updateLocalCircuitBreakerState(any(), any());
		verify(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		verify(circuitBreakerStockClient).getState();
		verify(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any());		
	}
    
    void execute_ShouldReturnPurchaseHistoryResponseDTO_WhenSynchronousRequestSucceedsAndCircuitBreakerIsHalfOpen() throws JsonProcessingException {

		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockUpdateResponseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		String circuitBreakerName = "circuitBreakerStockClient";
		String state ="HALF_OPEN";
		String resultRequest = "1";
		ResponseEntity<List<StockResponseDTO>> responseStock = new ResponseEntity<>(stockUpdateResponseDTOs,HttpStatus.OK);
		
		
		BDDMockito.when(redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient, circuitBreakerName)).thenReturn(state);
		BDDMockito.when(stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems())).thenReturn(responseStock);
		doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		doNothing().when(redisCircuitBreakerManager).halfOpenEvaluateCircuitTransition(circuitBreakerName);
		
		//ACT 
		List<StockResponseDTO> response = this.stockStep.execute(purchaseRequestDTO);
		
		//ASSERT
		verify(redisCircuitBreakerManager).halfOpenEvaluateCircuitTransition(circuitBreakerName);
		verify(redisCircuitBreakerManager).updateLocalCircuitBreakerState(any(), any());
		verify(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		verify(stockClient).updateStockAfterPurchase(purchaseRequestDTO.purchasedItems());
		Assertions.assertEquals(stockUpdateResponseDTOs, response, "Deveria retornar a resposta devolvida pelo microsserviço de stock");	
	}
	
    @Test
    @DisplayName("Should throw an AsynchronousProcessingException when the fallback is triggered, the CircuitBreaker is HALF_OPEN, and the synchronous request fails")
    void executeShouldThrowAsynchronousProcessingExceptionWhenFallbackIsTriggeredAndCircuitBreakerIsHalfOpenAndSynchronousRequestFails() 
    		throws JsonProcessingException {
		
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		String circuitBreakerName = "circuitBreakerStockClient";
		String state ="HALF_OPEN";
		String resultRequest = "0";	
		
		BDDMockito.when(stockClient.updateStockAfterPurchase(purchaseRequestDTO.purchasedItems())).thenThrow(ExceptionTestFactory.createServiceUnavailableCustomException());
		BDDMockito.when(redisCircuitBreakerManager.updateLocalCircuitBreakerState(circuitBreakerStockClient, circuitBreakerName)).thenReturn(state);	
		BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.HALF_OPEN);

		doThrow(ExceptionTestFactory.createAsynchronousProcessingException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(),any());
		
		doNothing().when(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		
		//ACT+ASSERT
		assertThrows(AsynchronousProcessingException.class, () -> stockStep.execute(purchaseRequestDTO));
		
		verify(redisCircuitBreakerManager).updateLocalCircuitBreakerState(any(), any());
		verify(redisCircuitBreakerManager).updateRequestResultsInRedis(resultRequest, circuitBreakerName);
		verify(circuitBreakerStockClient).getState();
		verify(circuitBreakerStrategyContext).setStrategy(state+"_CIRCUITBREAKERSTOCKCLIENT");
		verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any());		
	}
	
    @Test
    @DisplayName("Should throw AsynchronousProcessingException when fallbackMethod is triggered and CircuitBreaker is CLOSED")
    void fallBackMethodShouldThrowAsynchronousProcessingExceptionWhenCircuitBreakerIsClosed() throws JsonProcessingException {
    	
	   //ARRANGE
	   PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
	   String circuitBreakerName = "circuitBreakerStockClient";
	   String cirucitState = "CLOSED";
	   Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
	   String requestResult = "0";
	   
	   doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(anyString(), anyString());
	   BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.CLOSED);
	   
		doThrow(ExceptionTestFactory.createAsynchronousProcessingException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(),any());
	
		doNothing().when(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	   
	   //ACT
	  assertThrows(AsynchronousProcessingException.class, () -> 
	  	stockStep.fallBackMethod(purchaseRequestDTO,throwable));	
	  
	  verify(redisCircuitBreakerManager).updateRequestResultsInRedis(requestResult, circuitBreakerName);
	  verify(circuitBreakerStockClient).getState();
	  verify(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	  verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any());
   }
    
    @Test
    @DisplayName("Should throw AsynchronousProcessingException when fallbackMethod is triggered and CircuitBreaker is OPEN")
    void fallBackMethodShouldThrowAsynchronousProcessingExceptionWhenCircuitBreakerIsOpen() throws JsonProcessingException {
    	
	   //ARRANGE
	   PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
	   String circuitBreakerName = "circuitBreakerStockClient";
	   String cirucitState = "OPEN";
	   String requestResult = "0";
	   Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
	   
	   doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(anyString(), anyString());
	   BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.OPEN);
	   
		doThrow(ExceptionTestFactory.createAsynchronousProcessingException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(),any());
	
		doNothing().when(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	   
	   //ACT
	  assertThrows(AsynchronousProcessingException.class, () -> 
	  	stockStep.fallBackMethod(purchaseRequestDTO,throwable));	
	  
	  verify(redisCircuitBreakerManager).updateRequestResultsInRedis(requestResult, circuitBreakerName);
	  verify(circuitBreakerStockClient).getState();
	  verify(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	  verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any());
   }
	
    @Test
    @DisplayName("Should throw AsynchronousProcessingException when fallbackMethod is triggered and CircuitBreaker is OPEN")
    void fallBackMethodShouldThrowAsynchronousProcessingExceptionWhenCircuitBreakerIsHalfOpen() throws JsonProcessingException {
    	
	   //ARRANGE
	   PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
	   String circuitBreakerName = "circuitBreakerStockClient";
	   String cirucitState = "HALF_OPEN";
	   String requestResult = "0";
	   Throwable throwable = ExceptionTestFactory.createServiceUnavailableCustomException();
	   
	   doNothing().when(redisCircuitBreakerManager).updateRequestResultsInRedis(anyString(), anyString());
	   BDDMockito.when(circuitBreakerStockClient.getState()).thenReturn(CircuitBreaker.State.HALF_OPEN);
	   
	   doThrow(ExceptionTestFactory.createAsynchronousProcessingException())
			.when(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO),any(),any());
	
	   doNothing().when(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	   
	   //ACT
	   assertThrows(AsynchronousProcessingException.class, () -> 
	  	stockStep.fallBackMethod(purchaseRequestDTO,throwable));	
	  
	  verify(redisCircuitBreakerManager).updateRequestResultsInRedis(requestResult, circuitBreakerName);
	  verify(circuitBreakerStockClient).getState();
	  verify(circuitBreakerStrategyContext).setStrategy(cirucitState+"_CIRCUITBREAKERSTOCKCLIENT");
	  verify(circuitBreakerStrategyContext).executeStrategy(eq(purchaseRequestDTO), any(), any());
   }
}
