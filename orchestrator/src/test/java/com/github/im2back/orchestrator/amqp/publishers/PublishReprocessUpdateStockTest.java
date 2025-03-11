package com.github.im2back.orchestrator.amqp.publishers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class PublishReprocessUpdateStockTest {
	
	@InjectMocks
	private PublishReprocessUpdateStock publishReprocessUpdateStock;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@Mock
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Should correctly send reprocess update stock message to RabbitMQ")
	void shouldSendReprocessUpdateStock() throws JsonProcessingException {
		// ARRANGE
		ObjectMapper mapper = new ObjectMapper();
		
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		String json = mapper.writeValueAsString(purchaseRequestDTO);

		// Mockando comportamento do ObjectMapper
		BDDMockito.when(objectMapper.writeValueAsString(any())).thenReturn(json);
		doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

		// ACT
		publishReprocessUpdateStock.sendReprocessHistory(purchaseRequestDTO);

		// ASSERT
		verify(objectMapper).writeValueAsString(purchaseRequestDTO);
		verify(rabbitTemplate).convertAndSend("reprocess.steps.direct.exchange", "stock.reprocess.update.routing.key", json);
	}
}

