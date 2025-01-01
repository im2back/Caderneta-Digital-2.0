package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;

import com.github.im2back.stockms.model.entities.Product;

public record ProductDTO(

		Long id,

		String name,

		BigDecimal price,

		String code,

		Integer quantity,
	
		String productUrl

) {
	public ProductDTO(Product p) {
		this(p.getId(),p.getName(),p.getPrice(),p.getCode(),p.getQuantity(),p.getProductUrl());
	}
}
