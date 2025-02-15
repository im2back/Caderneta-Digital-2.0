package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatedProducts(
		
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
