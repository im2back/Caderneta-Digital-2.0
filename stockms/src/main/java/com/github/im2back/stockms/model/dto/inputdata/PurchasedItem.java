package com.github.im2back.stockms.model.dto.inputdata;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchasedItem(
		@NotBlank
		String code,
		
		@NotNull
		@Positive
		Integer quantity
		) {

}
