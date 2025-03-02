package com.github.im2back.orchestrator.service.circuitbreaker.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ValueOperations;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@ExtendWith(MockitoExtension.class)
class CircuitBreakerEventListenerTest {

    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private CircuitBreaker circuitBreakerCustomer;

    @Mock
    private CircuitBreaker circuitBreakerStock;

    @Mock
    private CircuitBreaker.EventPublisher customerEventPublisher;

    @Mock
    private CircuitBreaker.EventPublisher stockEventPublisher;

    private CircuitBreakerEventListener circuitBreakerEventListener;

    @BeforeEach
    void setup() {
        String circuitBreakerNameCustomer = "circuitBreakerCustomerClient";
        String circuitBreakerNameStock = "circuitBreakerStockClient";
 
        BDDMockito.when(circuitBreakerRegistry.circuitBreaker(circuitBreakerNameCustomer)).thenReturn(circuitBreakerCustomer);
        BDDMockito.when(circuitBreakerRegistry.circuitBreaker(circuitBreakerNameStock)).thenReturn(circuitBreakerStock);

        BDDMockito.when(circuitBreakerCustomer.getEventPublisher()).thenReturn(customerEventPublisher);
        BDDMockito.when(circuitBreakerStock.getEventPublisher()).thenReturn(stockEventPublisher);

        circuitBreakerEventListener = new CircuitBreakerEventListener(circuitBreakerRegistry, valueOperations,redissonClient);
    }

    @Test
    @DisplayName("Should register CircuitBreaker event listeners")
    void testRegisterListeners() {
        // ACT
        circuitBreakerEventListener.registerListeners();

        // ASSERT
        verify(circuitBreakerRegistry).circuitBreaker("circuitBreakerCustomerClient");
        verify(circuitBreakerRegistry).circuitBreaker("circuitBreakerStockClient");
        verify(customerEventPublisher).onStateTransition(any());
        verify(stockEventPublisher).onStateTransition(any());
    }
}


