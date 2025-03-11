package com.github.im2back.orchestrator.service.utils;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.dto.out.UpdatedProductsDTO;
import com.github.im2back.orchestrator.exception.customexceptions.GenericException;

public class Utils {

	public static PurchaseHistoryDTO assemblePurchaseHistoryDTO(PurchaseRequestDTO dto,List<StockResponseDTO> stockUpdateResponseDTOList) {
		
		if(stockUpdateResponseDTOList.isEmpty() || stockUpdateResponseDTOList == null) {
			throw new GenericException("Object StockUpdateResponseDTOList is invalid");
		}
		
		List<UpdatedProductsDTO> products = new ArrayList<>();
		stockUpdateResponseDTOList.forEach(t -> {
			products.add(new UpdatedProductsDTO(t.name(), t.price(), t.code(), t.quantity()));
		});

		PurchaseHistoryDTO purchaseHistoryDTO = new PurchaseHistoryDTO(dto.document(), products);

			return purchaseHistoryDTO;
	}

}
