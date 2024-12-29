package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;

import com.github.im2back.stockms.model.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockUpdateResponseDTO(

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
	 public StockUpdateResponseDTO(Product product, Integer quantity) {
		this(product.getName(), product.getPrice(), product.getCode(), quantity);
	}

}
