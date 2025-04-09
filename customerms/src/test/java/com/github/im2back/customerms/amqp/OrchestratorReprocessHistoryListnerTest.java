package com.github.im2back.customerms.amqp;

import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.datainput.PurchasedProductsDTO;
import com.github.im2back.customerms.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class OrchestratorReprocessHistoryListnerTest {
	
	@InjectMocks
	private OrchestratorReprocessHistoryListner orchestratorReprocessHistoryListner;
	
	@Mock
	private  CustomerService customerService;
		
	@Test
	void receiveShouldProcessMessageWhenReceivingValidMessage() throws IOException {
		//ARRANGE
		List<PurchasedProductsDTO> productsDTOs = new ArrayList<>();
		productsDTOs.add(new PurchasedProductsDTO("Nescau", new BigDecimal(10), "003", 2));
		PurchaseHistoryInDTO purchaseHistoryInDTO = new PurchaseHistoryInDTO("00769203213", productsDTOs);
		
		ObjectMapper mapper = new ObjectMapper();
		String purchaseHistoryInDTOJson = mapper.writeValueAsString(purchaseHistoryInDTO);
		
		//ACT
		this.orchestratorReprocessHistoryListner.receiveMessages(purchaseHistoryInDTOJson);
		
		//ASSERT
		verify(this.customerService).registerPurchaseAsync(purchaseHistoryInDTO);
	}

}
