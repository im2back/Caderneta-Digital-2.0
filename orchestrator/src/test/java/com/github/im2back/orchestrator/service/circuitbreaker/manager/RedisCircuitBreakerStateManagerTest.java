package com.github.im2back.orchestrator.service.circuitbreaker.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@ExtendWith(MockitoExtension.class)
class RedisCircuitBreakerStateManagerTest {
	@InjectMocks
	private RedisCircuitBreakerStateManager redisCircuitBreakerStateManager;

	@Mock
	private RedisTemplate<String, String> redisTemplate;
	@Mock
	private ValueOperations<String, String> valueOperations;
	@Mock
	private CircuitBreakerRegistry circuitBreakerRegistry;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private CircuitBreaker circuitBreakerCustomerClient;

	@BeforeEach
	void setUp() {
		Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		redisCircuitBreakerStateManager = new RedisCircuitBreakerStateManager(redisTemplate, circuitBreakerRegistry,
				objectMapper);
	}

	@Test
	@DisplayName("Should return a CircuitBreaker when the bean name is provided")
	void getCircuitBreakerShouldReturnCircuitBreakerWhenBeanNameIsProvided() throws JsonMappingException, JsonProcessingException {
		// ARRANGE
		String circuitName = "circuitBreakerCustomerClient";
		BDDMockito.when(redisCircuitBreakerStateManager.getCircuitBreaker(circuitName)).thenReturn(circuitBreakerCustomerClient);

		// ACT
		redisCircuitBreakerStateManager.getCircuitBreaker(circuitName);

		// ASSERT
		verify(circuitBreakerRegistry).circuitBreaker(circuitName);
	}

	@DisplayName("Should update the circuit request counter and save it in Redis")
	void closedEvaluateCircuitTransitionShouldUpdateCircuitRequestCounterAndSaveInRedis() throws JsonMappingException, JsonProcessingException {
	
		//ARRANGE 
		String circuitBreakerName = "circuitBreakerCustomerClient";
		String counterString = "2";
		String updatedCounter = "3";
		
		BDDMockito.when(valueOperations.get(anyString())).thenReturn(counterString);
		doNothing().when(valueOperations).set(anyString(), anyString());
		
		//ACT
		redisCircuitBreakerStateManager.closedEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":countBased");
		verify(valueOperations).set(circuitBreakerName + ":countBased", String.valueOf(updatedCounter));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should reset the metrics and keep the circuit state in CLOSED when the counter reaches 5 and failures are below 50%")
	void closedEvaluateCircuitTransitionShouldResetMetricsAndKeepCircuitStateInClosedWhenCounterReachesFiveAndFailuresAreBelow50() throws Exception {
	
		//ARRANGE 
		String counterString = "5";
		String circuitBreakerName = "circuitBreakerCustomerClient";
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> requestResultsList = new ArrayList<>(Arrays.asList("1","1","1","1","1"));
		String requestResultsListString = objectMapper.writeValueAsString(requestResultsList);
		
		BDDMockito.when(this.objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(requestResultsList);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":countBased")).thenReturn(counterString);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":resultRequest")).thenReturn(requestResultsListString);
		doNothing().when(valueOperations).set(anyString(), anyString());
		
		//ACT
		redisCircuitBreakerStateManager.closedEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":resultRequest");
		verify(valueOperations).set(circuitBreakerName + ":countBased", "0");
		verify(valueOperations).set(circuitBreakerName + ":resultRequest", "[]");	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should transition the circuit state to OPEN when the failure rate is above 50% and the counter is 5")
	void closedEvaluateCircuitTransitionShouldTransitionCircuitStateToOpenWhenFailureRateIsAbove50AndCounterIsFive() throws Exception {
	
		//ARRANGE 
		String counterString = "5";
		String circuitBreakerName = "circuitBreakerCustomerClient";
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> requestResultsList = new ArrayList<>(Arrays.asList("0","0","0","1","1"));
		String requestResultsListString = objectMapper.writeValueAsString(requestResultsList);
		
		BDDMockito.when(this.objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(requestResultsList);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":countBased")).thenReturn(counterString);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":resultRequest")).thenReturn(requestResultsListString);
		doNothing().when(valueOperations).set(anyString(), anyString());
		BDDMockito.when(circuitBreakerRegistry.circuitBreaker(circuitBreakerName)).thenReturn(circuitBreakerCustomerClient);
		doNothing().when(circuitBreakerCustomerClient).transitionToOpenState();		
		
		
		//ACT
		redisCircuitBreakerStateManager.closedEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":resultRequest");
		verify(valueOperations).set(circuitBreakerName + ":countBased", "0");
		verify(valueOperations).set(circuitBreakerName + ":resultRequest", "[]");
		verify(circuitBreakerRegistry).circuitBreaker(circuitBreakerName);
		verify(circuitBreakerCustomerClient).transitionToOpenState();
		verify(valueOperations).set(circuitBreakerName + ":state", "OPEN");
		
	}
		
	@Test
	@DisplayName("Should update the circuit request counter and save it in Redis")
	void halfOpenEvaluateCircuitTransitionShouldUpdateCircuitRequestCounterAndSaveInRedis() throws JsonMappingException, JsonProcessingException {
		//ARRANGE 
		String counterString = "1";
		String updatedCounter = "2";
		String circuitBreakerName = "circuitBreakerCustomerClient";
		
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":countBased")).thenReturn(counterString);
		doNothing().when(valueOperations).set(anyString(), anyString());
		
		//ACT
		redisCircuitBreakerStateManager.halfOpenEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":countBased");
		verify(valueOperations).set(circuitBreakerName + ":countBased", String.valueOf(updatedCounter));
	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should update the circuit request counter and save it in Redis")
	void halfOpenEvaluateCircuitTransitionShouldTransitionCircuitStateToClosedAndSaveInRedis() throws JsonMappingException, JsonProcessingException {
		//ARRANGE 
		String counterString = "2";
		String circuitBreakerName = "circuitBreakerCustomerClient";
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> requestResultsList = new ArrayList<>(Arrays.asList("1","1"));
		String requestResultsListString = objectMapper.writeValueAsString(requestResultsList);
		
		BDDMockito.when(this.objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(requestResultsList);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":resultRequest")).thenReturn(requestResultsListString);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":countBased")).thenReturn(counterString);
		BDDMockito.when(circuitBreakerRegistry.circuitBreaker(circuitBreakerName)).thenReturn(circuitBreakerCustomerClient);
		doNothing().when(valueOperations).set(anyString(), anyString());
		
		//ACT
		redisCircuitBreakerStateManager.halfOpenEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":countBased");
		verify(valueOperations).get(circuitBreakerName + ":resultRequest");
		verify(valueOperations).set(circuitBreakerName + ":countBased", "0");
		verify(valueOperations).set(circuitBreakerName + ":resultRequest", "[]");
		verify(circuitBreakerCustomerClient).transitionToClosedState();
		verify(valueOperations).set(circuitBreakerName + ":state", "CLOSED");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should update the circuit request counter and save it in Redis")
	void halfOpenEvaluateCircuitTransitionShouldTransitionCircuitStateToOpenAndSaveInRedis() throws JsonMappingException, JsonProcessingException {
		//ARRANGE 
		String counterString = "2";
		String circuitBreakerName = "circuitBreakerCustomerClient";
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> requestResultsList = new ArrayList<>(Arrays.asList("0","0"));
		String requestResultsListString = objectMapper.writeValueAsString(requestResultsList);
		
		BDDMockito.when(this.objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(requestResultsList);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":resultRequest")).thenReturn(requestResultsListString);
		BDDMockito.when(valueOperations.get(circuitBreakerName + ":countBased")).thenReturn(counterString);
		BDDMockito.when(circuitBreakerRegistry.circuitBreaker(circuitBreakerName)).thenReturn(circuitBreakerCustomerClient);
		doNothing().when(valueOperations).set(anyString(), anyString());
		
		//ACT
		redisCircuitBreakerStateManager.halfOpenEvaluateCircuitTransition(circuitBreakerName);
		
		//ASSERT
		verify(valueOperations).get(circuitBreakerName + ":countBased");
		verify(valueOperations).get(circuitBreakerName + ":resultRequest");
		verify(valueOperations).set(circuitBreakerName + ":countBased", "0");
		verify(valueOperations).set(circuitBreakerName + ":resultRequest", "[]");
		verify(circuitBreakerCustomerClient).transitionToOpenState();
		verify(valueOperations).set(circuitBreakerName + ":state", "OPEN");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should update the result request in Redis when updateRequestResultsInRedis is called")
	void updateRequestResultsInRedisShouldUpdateResultRequestInRedis() throws JsonProcessingException {
	    // ARRANGE
	    String result = "1";
	    String circuitBreakerName = "circuitBreakerCustomerClient";
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<String> requestResultsList = new ArrayList<>(Arrays.asList("1"));
	    String requestResultsListString = objectMapper.writeValueAsString(requestResultsList);
	    
	    List<String> requestResultsListPersist = new ArrayList<>(Arrays.asList("1", "1"));
	    String requestResultsListPersistString = objectMapper.writeValueAsString(requestResultsListPersist);
	    
	    doNothing().when(valueOperations).set(anyString(), anyString());
	    BDDMockito.when(this.objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(requestResultsList);
	    BDDMockito.when(valueOperations.get(circuitBreakerName + ":resultRequest")).thenReturn(requestResultsListString);
	    BDDMockito.when(this.objectMapper.writeValueAsString(requestResultsListPersist)).thenReturn(requestResultsListPersistString);
	    
	    // ACT
	    redisCircuitBreakerStateManager.updateRequestResultsInRedis(result, circuitBreakerName);
	    
	    // ASSERT
	    verify(valueOperations).set(circuitBreakerName + ":resultRequest", requestResultsListPersistString);
	}

	@Test
	@DisplayName("Should transition the circuit state to OPEN and return 'OPEN' when updateLocalCircuitBreakerState is called")
	void updateLocalCircuitBreakerStateShouldTransitionToOpenAndReturnState() throws JsonMappingException, JsonProcessingException {
	    // ARRANGE
	    String circuitBreakerName = "circuitBreakerCustomerClient";
	    BDDMockito.when(valueOperations.get(circuitBreakerName + ":state")).thenReturn("OPEN");
	    doNothing().when(circuitBreakerCustomerClient).transitionToOpenState();
	    
	    // ACT
	    String state = redisCircuitBreakerStateManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient, circuitBreakerName);
	    
	    // ASSERT
	    verify(circuitBreakerCustomerClient).transitionToOpenState();
	    Assertions.assertEquals("OPEN", state);	
	}

	@Test
	@DisplayName("Should transition the circuit state to CLOSED and return 'CLOSED' when updateLocalCircuitBreakerState is called")
	void updateLocalCircuitBreakerStateShouldTransitionToClosedAndReturnState() throws JsonMappingException, JsonProcessingException {
	    // ARRANGE
	    String circuitBreakerName = "circuitBreakerCustomerClient";
	    BDDMockito.when(valueOperations.get(circuitBreakerName + ":state")).thenReturn("CLOSED");
	    doNothing().when(circuitBreakerCustomerClient).transitionToClosedState();
	    
	    // ACT
	    String state = redisCircuitBreakerStateManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient, circuitBreakerName);
	    
	    // ASSERT
	    verify(circuitBreakerCustomerClient).transitionToClosedState();
	    Assertions.assertEquals("CLOSED", state);	
	}

	@Test
	@DisplayName("Should transition the circuit state to HALF_OPEN and return 'HALF_OPEN' when updateLocalCircuitBreakerState is called")
	void updateLocalCircuitBreakerStateShouldTransitionToHalfOpenAndReturnState() throws JsonMappingException, JsonProcessingException {
	    // ARRANGE
	    String circuitBreakerName = "circuitBreakerCustomerClient";
	    BDDMockito.when(valueOperations.get(circuitBreakerName + ":state")).thenReturn("HALF_OPEN");
	    doNothing().when(circuitBreakerCustomerClient).transitionToHalfOpenState();
	    
	    // ACT
	    String state = redisCircuitBreakerStateManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient, circuitBreakerName);
	    
	    // ASSERT
	    verify(circuitBreakerCustomerClient).transitionToHalfOpenState();
	    Assertions.assertEquals("HALF_OPEN", state);	
	}

	@Test
	@DisplayName("Should throw a CircuitBreakerCustomException when the circuit state is unknown during updateLocalCircuitBreakerState invocation")
	void updateLocalCircuitBreakerStateShouldThrowCircuitBreakerCustomExceptionForUnknownState() throws JsonProcessingException {
	    // ARRANGE
	    String circuitBreakerName = "circuitBreakerCustomerClient";
	    BDDMockito.when(valueOperations.get(circuitBreakerName + ":state")).thenReturn("DESCONHECIDO");
	    
	    // ACT + ASSERT
	    Assertions.assertThrows(CircuitBreakerCustomException.class, 
	        () -> redisCircuitBreakerStateManager.updateLocalCircuitBreakerState(circuitBreakerCustomerClient, circuitBreakerName));
	}	
}
