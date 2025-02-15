package com.github.im2back.orchestrator.dto.out;

import java.util.List;



import jakarta.validation.constraints.NotBlank;

public record PurchaseHistoryDTO(
		@NotBlank
		String document,	
		List<UpdatedProductsDTO> products
		
		) {

	
}
