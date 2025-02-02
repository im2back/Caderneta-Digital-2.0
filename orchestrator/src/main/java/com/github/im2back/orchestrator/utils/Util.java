package com.github.im2back.orchestrator.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class Util {

	  private Util() {}

	    public static String getCircuitBreakerState(CircuitBreakerRegistry circuitBreakerRegistry, String circuitBreakerName) {    
	        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
	        // Retorna o estado (CLOSED, OPEN, HALF_OPEN)
	        return circuitBreaker.getState().name();
	    } 	
	
}
