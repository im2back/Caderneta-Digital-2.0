package com.github.im2back.orchestrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

@Configuration
public class Resilience4jConfig {

    @Bean
    public Retry retryCustomerClient(RetryRegistry retryRegistry) {
        return retryRegistry.retry("retryCustomerClient");
    }  
    
    @Bean(name = "retryStockClient")
    public Retry retryStockClient(RetryRegistry retryRegistry) {
        return retryRegistry.retry("retryStockClient");
    }

    @Bean(name = "circuitBreakerCustomerClient")
    public CircuitBreaker circuitBreakerCustomerClient(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker("circuitBreakerCustomerClient");
    }

    @Bean(name = "circuitBreakerStockClient")
    public CircuitBreaker circuitBreakerStockClient(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker("circuitBreakerStockClient");
    }
 
}
