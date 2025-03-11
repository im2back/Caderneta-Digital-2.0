package com.github.im2back.orchestrator.service.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.im2back.orchestrator.dto.in.PurchaseRequestDTO;
import com.github.im2back.orchestrator.dto.in.StockResponseDTO;
import com.github.im2back.orchestrator.dto.out.PurchaseHistoryDTO;
import com.github.im2back.orchestrator.util.PurchaseTestFactory;

class UtilsTest {

	@Test
	@DisplayName("Should construct a PurchaseHistoryDTO from the given parameters")
	void assemblePurchaseHistoryDTOshouldConstructPurchaseHistoryDTOFromGivenParameters() {
		//ARRANGE
		PurchaseRequestDTO purchaseRequestDTO = PurchaseTestFactory.createValidPurchaseRequestDTO();
		List<StockResponseDTO> stockResponseDTO = PurchaseTestFactory.createSuccessfulStockUpdateResponse();
		Map<String,StockResponseDTO> StockResponseDTOMap =  stockResponseDTO.stream().collect(Collectors.toMap(StockResponseDTO::code,Function.identity()));
				
		//ACT
		PurchaseHistoryDTO response = Utils.assemblePurchaseHistoryDTO(purchaseRequestDTO, stockResponseDTO);
		var productsList = response.products();
		
		//ASSERT
		Assertions.assertEquals(response.document(), purchaseRequestDTO.document(), "O documento do saída deve corresponder ao de entrada.");
		Assertions.assertEquals(response.products().size(), stockResponseDTO.size(), "A quantidade de produtos de saída deve ser igual a de entrada");		
		productsList.forEach(p -> {Assertions.assertTrue(StockResponseDTOMap.containsKey(p.code()));});
	}

}
