package com.github.im2back.stockms.model.dto.outputdata;

import java.util.List;



import jakarta.validation.constraints.NotBlank;

public record PurchaseHistoryDTO(
		@NotBlank
		String document,	
		List<UpdatedProducts> products
		
		) {

	
}
