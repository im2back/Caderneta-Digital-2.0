package com.github.im2back.orchestrator.service.utils;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockUpdateResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.dto.out.UpdatedProductsDTO;

public class ServiceUtils {

	public static PurchaseHistoryDTO assemblePurchaseHistoryDTO(PurchaseRequestDTO dto, List<StockUpdateResponseDTO> stockUpdateResponseDTOList) {
		List<UpdatedProductsDTO> products = new ArrayList<>();
		
		stockUpdateResponseDTOList.forEach(t -> {
			products.add(new UpdatedProductsDTO(t.name(), t.price(), t.code(), t.quantity()));
		});
		
		PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);
		
		return purchaseHistoryDTO;
	}

	
}
