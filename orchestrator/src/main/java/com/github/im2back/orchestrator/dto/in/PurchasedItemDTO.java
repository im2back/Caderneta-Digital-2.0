package com.github.im2back.orchestrator.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchasedItemDTO(
		@NotBlank
		String code,
		
		@NotNull
		@Positive
		Integer quantity
		) {

}
