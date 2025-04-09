package com.github.im2back.customerms.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrchestratorReprocessHistoryListner {
	
	private final CustomerService customerService;
	
	@RabbitListener(queues = "customer.history.queue")
	public void receiveMessages(@Payload String msg) throws JsonProcessingException {
		PurchaseHistoryInDTO purchaseHistoryInDTO = convert(msg);
		customerService.registerPurchaseAsync(purchaseHistoryInDTO);
	}
	
	private PurchaseHistoryInDTO convert(String payload) throws  JsonProcessingException {
		var mapper = new ObjectMapper();
		return mapper.readValue(payload, PurchaseHistoryInDTO.class);
	}

}
