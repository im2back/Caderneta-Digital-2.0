package com.github.im2back.orchestrator.service.circuitbreaker.manager;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.exception.customexceptions.CircuitBreakerCustomException;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
public class RedisCircuitBreakerStateManager {
	

	private final ValueOperations<String, String> valueOperations;
	private final CircuitBreakerRegistry circuitBreakerRegistry;
	private final ObjectMapper objectMapper;

	public RedisCircuitBreakerStateManager(RedisTemplate<String, String> redisTemplate,
			CircuitBreakerRegistry circuitBreakerRegistry, ObjectMapper objectMapper) {
		this.valueOperations = redisTemplate.opsForValue();
		this.circuitBreakerRegistry = circuitBreakerRegistry;
		this.objectMapper = objectMapper; 
	}

	public CircuitBreaker getCircuitBreaker(String name) throws JsonMappingException, JsonProcessingException {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
		return circuitBreaker;
	}

	public void closedEvaluateCircuitTransition(String circuitBreakerName)throws JsonMappingException, JsonProcessingException {

		String counterString = valueOperations.get(circuitBreakerName + ":countBased");
		Integer counterInt = Integer.parseInt(counterString);
		int failedRequestsCount = 0;

		if (counterInt < 5) {
			Integer updatedCounter = counterInt + 1;
			valueOperations.set(circuitBreakerName + ":countBased", String.valueOf(updatedCounter));
		}

		if (counterInt.equals(5)) {
			String requestResultsList = valueOperations.get(circuitBreakerName + ":resultRequest");
			List<String> requestResultsListConverted = objectMapper.readValue(requestResultsList,new TypeReference<List<String>>() {});

			for (String result : requestResultsListConverted) {
				if (result.equals("0")) {
					failedRequestsCount = failedRequestsCount + 1;
				}
			}

			if (failedRequestsCount >= 3) {
				CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
				circuitBreaker.transitionToOpenState();
				valueOperations.set(circuitBreakerName + ":state", "OPEN");
			}
			resetMetrics(circuitBreakerName);
		}
	}

	public void halfOpenEvaluateCircuitTransition(String circuitBreakerName) throws JsonMappingException, JsonProcessingException {
		
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
		String counterString = valueOperations.get(circuitBreakerName + ":countBased");
		Integer counterInt = Integer.parseInt(counterString);
		int failedRequestsCount = 0;

		if (counterInt < 2) {
			Integer updatedCounter = counterInt + 1;
			valueOperations.set(circuitBreakerName + ":countBased", String.valueOf(updatedCounter));
		}

		if (counterInt >= 2) {
			String requestResultsList = valueOperations.get(circuitBreakerName + ":resultRequest");
			List<String> requestResultsListConverted = objectMapper.readValue(requestResultsList, new TypeReference<List<String>>() {});

			for (String result : requestResultsListConverted) {
				if (result.equals("0")) {
					failedRequestsCount = failedRequestsCount + 1;
				}
			}

			if (failedRequestsCount != 0) {
				circuitBreaker.transitionToOpenState();
				valueOperations.set(circuitBreakerName + ":state", "OPEN");
			} else {
				circuitBreaker.transitionToClosedState();
				valueOperations.set(circuitBreakerName + ":state", "CLOSED");
			}
			resetMetrics(circuitBreakerName);
		}
	}

	public void updateRequestResultsInRedis(String result, String circuitBreakerName) throws JsonProcessingException, JsonMappingException {
		String requestResultJson = valueOperations.get(circuitBreakerName+ ":resultRequest");
		List<String> requestResultObject = objectMapper.readValue(requestResultJson, new TypeReference<List<String>>() {});
		requestResultObject.add(result);
		valueOperations.set(circuitBreakerName + ":resultRequest", objectMapper.writeValueAsString(requestResultObject));
	}

	public String updateLocalCircuitBreakerState(CircuitBreaker circuitBreaker,String circuitBreakerName) {
		
		String currentCircuitStateFromRedis = valueOperations.get(circuitBreakerName + ":state");
		
		switch(currentCircuitStateFromRedis) {	
			case "OPEN" -> {
				circuitBreaker.transitionToOpenState();
				return "OPEN";
			}
			
			case "CLOSED" -> {
				circuitBreaker.transitionToClosedState();
				return "CLOSED";
			}
			
			case "HALF_OPEN" -> {
				circuitBreaker.transitionToHalfOpenState();
				return "HALF_OPEN";
			}
			default -> throw new CircuitBreakerCustomException("Update Status Manager - Status desconhecido: " + currentCircuitStateFromRedis);
		}
		
	}
	
	private void resetMetrics(String circuitBreakerName) {
		valueOperations.set(circuitBreakerName + ":countBased", "0");
		valueOperations.set(circuitBreakerName + ":resultRequest", "[]");
	}
}
