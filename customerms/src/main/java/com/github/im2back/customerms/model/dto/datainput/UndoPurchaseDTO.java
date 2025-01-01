package com.github.im2back.customerms.model.dto.datainput;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UndoPurchaseDTO(
		@NotNull
		Long purchaseId,
		
		@NotBlank
		String productCode,
		
		@NotNull
		Integer quantity
		) {

}
