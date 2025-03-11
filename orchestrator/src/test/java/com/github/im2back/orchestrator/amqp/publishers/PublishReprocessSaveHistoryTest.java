package com.github.im2back.orchestrator.amqp.publishers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

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
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.service.utils.Utils;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

@ExtendWith(MockitoExtension.class)
class PublishReprocessSaveHistoryTest {
	
	@InjectMocks
	private PublishReprocessSaveHistory publishReprocessSaveHistory;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@Mock
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Should correctly send message to RabbitMQ")
	void shouldSendReprocessHistory() throws JsonProcessingException {
		// ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockResponseDTOs = PurchaseTestFactory.createSuccessfulStockUpdateResponse();		
		PurchaseHistoryDTO purchaseHistoryDTO = Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseDTOs);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(purchaseHistoryDTO);

		// Mockando comportamento do ObjectMapper
		BDDMockito.when(objectMapper.writeValueAsString(any())).thenReturn(json);
		doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

		// ACT
		publishReprocessSaveHistory.sendReprocessHistory(purchaseHistoryDTO);

		// ASSERT
		verify(objectMapper).writeValueAsString(purchaseHistoryDTO);
		verify(rabbitTemplate).convertAndSend("reprocess.steps.direct.exchange", "customer.reprocess.history.routing.key", json);
	}
}
