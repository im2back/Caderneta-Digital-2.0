package com.github.im2back.stockms.model.dto.inputdata;

import java.math.BigDecimal;

import com.github.im2back.stockms.model.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewProductToSaveDTO(

		@NotBlank 
		String name,

		@NotNull
		BigDecimal price,

		@NotBlank
		String code,

		@NotNull
		@Positive
		Integer quantity,
		
		String productUrl
		) {
	
	 public NewProductToSaveDTO(Product product, Integer quantity) {
		this(product.getName(), product.getPrice(), product.getCode(), quantity,product.getProductUrl());
	}

}
