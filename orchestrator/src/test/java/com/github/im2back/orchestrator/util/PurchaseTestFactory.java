package com.github.im2back.orchestrator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.orchestrator.dto.in.PurchaseHistoryResponseDTO;
import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.PurchasedItemDTO;
import com.github.im2back.orchestrator.dto.in.PurchasedProductDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;

public class PurchaseTestFactory {


	public static List<StockResponseDTO> createSuccessfulStockUpdateResponse() {
		List<StockResponseDTO>  list = new ArrayList<>();
		list.add(new StockResponseDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
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
		
		return new PurchaseHistoryResponseDTO("Jefferson Souza", produtosComprados, total);
	}
	
	public static List<StockResponseDTO> createNullStockUpdateResponse() {
		List<StockResponseDTO>  list = null;
		return list;
	}
		
	public static PurchaseRequestDTO createInvalidPurchaseRequestDTO() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("001", 1000));
			return new PurchaseRequestDTO("00769203213", purchasedItemsDto);
	}
	
	public static PurchaseRequestDTO createInvalidPurchaseRequestDTOUserNotExist() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("001", 10));
			return new PurchaseRequestDTO("00969503240", purchasedItemsDto);
	}
	
	public static PurchaseRequestDTO createValidPurchaseRequestDTO() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("001", 10));
			return new PurchaseRequestDTO("00769203213", purchasedItemsDto);
	}
	
	public static PurchaseHistoryResponseDTO createPurchaseHistoryResponseDTOSuccessful() {
		PurchasedProductDTO productDTO1 = new PurchasedProductDTO("Nescau", 10, new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP));
		List<PurchasedProductDTO> productDTOs = new ArrayList<>();
		productDTOs.add(productDTO1);

		
		return new PurchaseHistoryResponseDTO("Jefferson Souza", productDTOs, new BigDecimal(199.90).setScale(2, RoundingMode.HALF_UP));
	}
	
	public static PurchaseRequestDTO createInvalidPurchaseRequestDTOProductNotExist() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("0070", 10));
			return new PurchaseRequestDTO("00769203213", purchasedItemsDto);
	}
	
	public static PurchaseRequestDTO createInvalidPurchaseRequestDTOUserInactive() {
		List<PurchasedItemDTO> purchasedItemsDto = new ArrayList<>();
		purchasedItemsDto.add(new PurchasedItemDTO("001", 1));
			return new PurchaseRequestDTO("00869205210", purchasedItemsDto);
	}

}
