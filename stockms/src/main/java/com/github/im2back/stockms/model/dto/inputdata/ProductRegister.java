package com.github.im2back.stockms.model.dto.inputdata;

import java.math.BigDecimal;

import com.github.im2back.stockms.model.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRegister(

		@NotBlank 
		String name,

		@NotBlank 
		BigDecimal price,

		@NotBlank
		String code,

		@NotNull
		@Positive
		Integer quantity) {
	
	 public ProductRegister(Product product, Integer quantity) {
		this(product.getName(), product.getPrice(), product.getCode(), quantity);
	}

}
