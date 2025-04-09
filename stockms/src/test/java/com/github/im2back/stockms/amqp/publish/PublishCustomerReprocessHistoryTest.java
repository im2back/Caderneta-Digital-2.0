package com.github.im2back.stockms.amqp.publish;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseHistoryDTO;
import com.github.im2back.stockms.utils.PurchaseDtosFactory;

@ExtendWith(MockitoExtension.class)
class PublishCustomerReprocessHistoryTest {

	@InjectMocks
	private PublishCustomerReprocessHistory publishCustomerReprocessHistory;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	@DisplayName("Should send reprocess save history message successfully when given a valid purchase history DTO")
	void sendReprocessSaveHistory_ShouldSendMessageSuccessfully_whenGivenValidPurchaseHistoryDTO() throws JsonProcessingException {
		//ARRANGE
		
		PurchaseHistoryDTO purchaseHistoryDTO = PurchaseDtosFactory.createPurchaseHistoryDTO();
		String jsonEsperado = this.mapper.writeValueAsString(purchaseHistoryDTO);
		
		doNothing().when(this.rabbitTemplate).convertAndSend(any(), any(),anyString());
		
		//ACT
		this.publishCustomerReprocessHistory.sendReprocessSaveHistory(purchaseHistoryDTO);
		
		
		//ASSERT
		//para não utilizar o contexto spring foi ignorado a verificação do @Value
		verify(this.rabbitTemplate).convertAndSend(null,null,jsonEsperado);
	}

}
