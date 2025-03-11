package com.github.im2back.orchestrator.amqp.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublishReprocessSaveHistory {
	
		private final RabbitTemplate rabbitTemplate;
		private final ObjectMapper objectMapper;

		private String convertIntoJson(PurchaseHistoryDTO data) throws JsonProcessingException {
			String json = objectMapper.writeValueAsString(data);		
			return json;	
		}	
		public void sendReprocessHistory(PurchaseHistoryDTO data) throws JsonProcessingException {
			String json = convertIntoJson(data);
			rabbitTemplate.convertAndSend("reprocess.steps.direct.exchange","customer.reprocess.history.routing.key",json);
		}
}
