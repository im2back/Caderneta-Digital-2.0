package com.github.im2back.stockms.model.dto.inputdata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UndoPurchaseDto(
		@NotNull
		Long purchaseId,
		
		@NotBlank
		String productCode,
		
		@NotNull
		Integer quantity
		) {

}
