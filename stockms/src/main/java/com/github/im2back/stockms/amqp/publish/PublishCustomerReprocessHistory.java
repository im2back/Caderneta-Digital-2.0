package com.github.im2back.stockms.amqp.publish;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseHistoryDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublishCustomerReprocessHistory {

	private final RabbitTemplate rabbitTemplate;
	
	private String convert(PurchaseHistoryDTO data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		var json = mapper.writeValueAsString(data);		
		return json;
	}
	
	public void sendReprocessSaveHistory(PurchaseHistoryDTO data) throws JsonProcessingException {
		String json = convert(data);
		rabbitTemplate.convertAndSend("reprocess.steps.direct.exchange","customer.reprocess.history.routing.key",json);
	}
	

	
}
