package com.github.im2back.orchestrator.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerAMQPConfiguration {

	@Bean
	public Queue createQueueHistoryReprocess() {
		return new Queue("customer.history.queue", true);
	}
	
	@Bean
	public Queue createQueueStockReprocess() {
		return new Queue("stock.update.queue", true);
	}

	@Bean
	public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
		return new RabbitAdmin(conn);
	}

	@Bean
	public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
	    return event -> {
	        rabbitAdmin.declareQueue(createQueueHistoryReprocess()); // Declara a fila
	        rabbitAdmin.declareQueue(createQueueStockReprocess()); // Declara a fila
	        
	        rabbitAdmin.declareExchange(createExchangeReprocessSteps()); // Declara o exchange
	        
	        rabbitAdmin.declareBinding(bindHistoryReprocessQueueToExchange(createQueueHistoryReprocess(), createExchangeReprocessSteps())); 
	        rabbitAdmin.declareBinding(bindStockReprocessQueueToExchange(createQueueStockReprocess(), createExchangeReprocessSteps())); // Declara o binding
	    };
	}

    @Bean
    public DirectExchange createExchangeReprocessSteps() {
        return new DirectExchange("reprocess.steps.direct.exchange");
    }
    
    @Bean
    public Binding bindHistoryReprocessQueueToExchange(Queue createQueueHistoryReprocess, DirectExchange createExchangeHistoryReprocess) {   
        return BindingBuilder.bind(createQueueHistoryReprocess).to(createExchangeHistoryReprocess).with("customer.reprocess.history.routing.key");
    }
    
    @Bean
    public Binding bindStockReprocessQueueToExchange(Queue createQueueStockReprocess, DirectExchange createExchangeReprocessSteps) {   
        return BindingBuilder.bind(createQueueStockReprocess).to(createExchangeReprocessSteps).with("stock.reprocess.update.routing.key");
    }
}
