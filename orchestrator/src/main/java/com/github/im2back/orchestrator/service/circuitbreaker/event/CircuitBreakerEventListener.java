package com.github.im2back.orchestrator.service.circuitbreaker.event;


import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.ValueOperations;
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
    private final ValueOperations<String, String> valueOperations;
    private final RedissonClient redissonClient; 
    
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
            RLock lock = redissonClient.getLock("lock:" + circuitBreakerName);
            try {
                if (lock.tryLock(0, 30, TimeUnit.SECONDS)) { // Evita que múltiplas instâncias executem esse código simultaneamente
                    Thread.sleep(30000); 
                    circuitBreaker.transitionToHalfOpenState();
                    valueOperations.set(circuitBreakerName + ":state", "HALF_OPEN");
                    valueOperations.set(circuitBreakerName + ":countBased", "0");
                    valueOperations.set(circuitBreakerName + ":resultRequest", "[]");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }).start();
    }
}

