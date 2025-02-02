package com.github.im2back.orchestrator.service.circuitbreaker.config;


import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CircuitBreakerEventListener {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void registerListeners() {
        String circuitBreakerNameCustomer = "circuitBreakerCustomerClient";
        String circuitBreakerNameStock = "circuitBreakerStockClient";
        
        
        CircuitBreaker circuitBreakerCustomer = circuitBreakerRegistry.circuitBreaker(circuitBreakerNameCustomer);
        CircuitBreaker circuitBreakerStock = circuitBreakerRegistry.circuitBreaker(circuitBreakerNameStock);

        circuitBreakerCustomer.getEventPublisher()
                .onStateTransition(this::onStateTransition);
        
        circuitBreakerStock.getEventPublisher()
        .onStateTransition(this::onStateTransition);
    }

    private void onStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        CircuitBreaker.State currentState = event.getStateTransition().getToState();

        if (currentState == CircuitBreaker.State.OPEN) {
            forceHalfOpenAfterDelay(event.getCircuitBreakerName());
        }
    }

    private void forceHalfOpenAfterDelay(String circuitBreakerName) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);

        new Thread(() -> {
            try {
                Thread.sleep(30000); 
                circuitBreaker.transitionToHalfOpenState();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

