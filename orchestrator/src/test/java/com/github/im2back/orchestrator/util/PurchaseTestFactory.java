package com.github.im2back.orchestrator.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.PurchasedItemDTO;
import com.github.im2back.orchestrator.dto.in.PurchasedProductDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;

public class PurchaseTestFactory {

	public static PurchaseRequestDTO createValidPurchaseRequestDTO() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("001", 10));
			return new PurchaseRequestDTO("12345679", purchasedItemsDto);
	}
	
	public static List<StockResponseDTO> createSuccessfulStockUpdateResponse() {
		List<StockResponseDTO>  list = new ArrayList<>();
		list.add(new StockResponseDTO("Nescau", new BigDecimal(5), "001", 10));
		return list;
	}
	
	public static PurchaseHistoryResponseDTO createSuccessfulPurchaseHistory() {
		
		List<StockResponseDTO> response =  createSuccessfulStockUpdateResponse();
		List<PurchasedProductDTO> produtosComprados = new ArrayList<>();
		BigDecimal  total= new BigDecimal(0);
		
		response.forEach(t -> {
			produtosComprados.add(new PurchasedProductDTO(t.name(), t.quantity(), t.price()));
			total.add(t.price());
		});
		
		return new PurchaseHistoryResponseDTO("Jefferson", produtosComprados, total);
	}
	
	public static List<StockResponseDTO> createNullStockUpdateResponse() {
		List<StockResponseDTO>  list = null;
		return list;
	}
	
	public static PurchaseHistoryResponseDTO createPurchaseHistoryResponseDTOSuccessful() {
		PurchasedProductDTO productDTO1 = new PurchasedProductDTO("Nescau", 2, new BigDecimal(10));
		PurchasedProductDTO productDTO2 = new PurchasedProductDTO("Arroz", 3, new BigDecimal(5));
		List<PurchasedProductDTO> productDTOs = new ArrayList<>();
		productDTOs.add(productDTO1);
		productDTOs.add(productDTO2);
		
		return new PurchaseHistoryResponseDTO("Jefferson", productDTOs, new BigDecimal(100));
	}	

}
