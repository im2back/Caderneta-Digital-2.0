package com.github.im2back.stockms.amqp.listner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.im2back.stockms.amqp.publish.PublishCustomerReprocessHistory;
import com.github.im2back.stockms.model.dto.inputdata.PurchaseRequestDTO;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseHistoryDTO;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedStockResponseDTO;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedProducts;
import com.github.im2back.stockms.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrchestratorUpdateStockReprocessListner {

	private final ProductService productService;
	private final PublishCustomerReprocessHistory customerReprocessHistory;
	
	@RabbitListener(queues = "stock.update.queue")
	public void receiveMessages(@Payload String msg) throws JsonMappingException, JsonProcessingException {

		PurchaseRequestDTO purchaseRequestDTO = convert(msg);
		List<UpdatedStockResponseDTO> response = this.productService.updateQuantityProductsAfterPurchase(purchaseRequestDTO.purchasedItems());
		PurchaseHistoryDTO purchaseHistoryDTO = assemblePurchaseHistoryDTO(purchaseRequestDTO,response);
		this.customerReprocessHistory.sendReprocessSaveHistory(purchaseHistoryDTO);
	}
	
	private PurchaseRequestDTO convert(String payload) throws JsonMappingException, JsonProcessingException {
		var mapper = new ObjectMapper();
		 return mapper.readValue(payload, PurchaseRequestDTO.class);
	}
	
	private PurchaseHistoryDTO assemblePurchaseHistoryDTO(PurchaseRequestDTO dto, List<UpdatedStockResponseDTO> stockUpdateResponseDTOList) {
		
		List<UpdatedProducts> products = stockUpdateResponseDTOList.stream()
			    .map(t -> new UpdatedProducts(t.name(), t.price(), t.code(), t.quantity()))
			    .collect(Collectors.toList());
		
		PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);
		
		return purchaseHistoryDTO;
	}

}
