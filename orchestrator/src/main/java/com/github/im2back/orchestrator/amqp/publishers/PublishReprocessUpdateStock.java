package com.github.im2back.orchestrator.amqp.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublishReprocessUpdateStock {
	
		private final RabbitTemplate rabbitTemplate;

		private String convertIntoJson(PurchaseRequestDTO  data) throws JsonProcessingException {
			ObjectMapper mapper = new ObjectMapper();
			var json = mapper.writeValueAsString(data);		
			return json;	
		}	

		public void sendReprocessHistory(PurchaseRequestDTO  data) throws JsonProcessingException {
			var json = convertIntoJson(data);
			rabbitTemplate.convertAndSend("reprocess.steps.direct.exchange","stock.reprocess.update.routing.key",json);
		}
}
