package com.github.im2back.stockms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.im2back.stockms.model.dto.outputdata.PurchaseHistoryDTO;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedProducts;
import com.github.im2back.stockms.model.dto.outputdata.UpdatedStockResponseDTO;

public class PurchaseDtosFactory {

	
	public static PurchaseHistoryDTO createPurchaseHistoryDTO() {
		
		
		List<UpdatedStockResponseDTO> dtos = new ArrayList<>();
		dtos.add(new UpdatedStockResponseDTO("Nescau", new BigDecimal("19.99").setScale(2, RoundingMode.UP), "001", 2));
		dtos.add(new UpdatedStockResponseDTO("Arroz", new BigDecimal("10.99").setScale(2, RoundingMode.UP), "003", 2));
		
		List<UpdatedProducts> products = dtos.stream()
			    .map(t -> new UpdatedProducts(t.name(), t.price(), t.code(), t.quantity()))
			    .collect(Collectors.toList());
		
		return new PurchaseHistoryDTO("00769203213", products);
	}
	
	
}
