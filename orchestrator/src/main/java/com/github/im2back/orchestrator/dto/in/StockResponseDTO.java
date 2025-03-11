package com.github.im2back.orchestrator.dto.in;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockResponseDTO(

		@NotBlank 
		String name,

		@NotNull
		BigDecimal price,

		@NotBlank
		String code,

		@NotNull
		@Positive
		Integer quantity
		
		) {

}
