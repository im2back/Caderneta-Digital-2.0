package com.github.im2back.orchestrator.dto.out;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatedProductsDTO(
		
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
