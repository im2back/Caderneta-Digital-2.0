package com.github.im2back.customerms.model.dto.datainput;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchasedProductsDTO(
		
		@NotBlank
		String name,
		
		@NotNull
		BigDecimal price,
		
		@NotBlank
		String code,
		
		@NotNull
		Integer quantity
		) {

}
