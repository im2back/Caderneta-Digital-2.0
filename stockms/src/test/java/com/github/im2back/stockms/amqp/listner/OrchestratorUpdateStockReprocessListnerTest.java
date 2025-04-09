package com.github.im2back.stockms.amqp.listner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.amqp.publish.PublishCustomerReprocessHistory;
import com.github.im2back.stockms.model.dto.inputdata.PurchaseRequestDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseHistoryDTO;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedStockResponseDTO;
import com.github.im2back.stockms.service.ProductService;
import com.github.im2back.stockms.utils.ProductFactoryTest;


@ExtendWith(MockitoExtension.class)
class OrchestratorUpdateStockReprocessListnerTest {

	@InjectMocks
	private OrchestratorUpdateStockReprocessListner orchestratorUpdateStockReprocessListner;
	
	@Mock
	private ProductService productService;
	
	@Mock
	private PublishCustomerReprocessHistory customerReprocessHistory;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Captor
	private ArgumentCaptor<PurchaseHistoryDTO> captorPurchaseHistoryDTO;
	
	@Test
	@DisplayName("Should update product quantities after purchase successfully when given a valid purchase message")
	void updateProductQuantitiesAfterPurchase_ShouldCompleteSuccessfully_whenGivenValidPurchaseMessage() throws IOException {
	    // ARRANGE
		//ARRANGE
		String document = "00769203213";
		List<PurchasedItemDTO> list = ProductFactoryTest.createListPurchasedItemDTO();
		PurchaseRequestDTO purchaseRequestDTO = new PurchaseRequestDTO(document, list);
		
		
		String msg = this.mapper.writeValueAsString(purchaseRequestDTO);
		
		List<UpdatedStockResponseDTO> dtos = new ArrayList<>();
		dtos.add(new UpdatedStockResponseDTO("Nescau", new BigDecimal("19.99").setScale(2, RoundingMode.UP), "001", 2));
		dtos.add(new UpdatedStockResponseDTO("Arroz", new BigDecimal("10.99").setScale(2, RoundingMode.UP), "003", 2));
		
		BDDMockito.when(this.productService.updateQuantityProductsAfterPurchase(any())).thenReturn(dtos);
		BDDMockito.doNothing().when(this.customerReprocessHistory).sendReprocessSaveHistory(any());
		
		
		//ACT
		this.orchestratorUpdateStockReprocessListner.receiveMessages(msg);
		
		//ASSERT
		verify(this.productService).updateQuantityProductsAfterPurchase(list);
		
		verify(this.customerReprocessHistory).sendReprocessSaveHistory(this.captorPurchaseHistoryDTO.capture());
		PurchaseHistoryDTO purchaseHistoryDTOCaptured = this.captorPurchaseHistoryDTO.getValue();
		Assertions.assertEquals("00769203213", purchaseHistoryDTOCaptured.document());
		Assertions.assertEquals(2, purchaseHistoryDTOCaptured.products().size(), "Verifica se a resposta tem o mesmo número de produtos comprados");
		
		purchaseHistoryDTOCaptured.products().forEach(t -> {
			Assertions.assertTrue(t.code().equals("001") || t.code().equals("003"), "Verifica se o retorno contem todos produtos");
		});
		
		
	}

}
