package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;

import com.github.im2back.stockms.model.entities.Product;

public record ProductDto(

		Long id,

		String name,

		BigDecimal price,

		String code,

		Integer quantity

) {
	public ProductDto(Product p) {
		this(p.getId(),p.getName(),p.getPrice(),p.getCode(),p.getQuantity());
	}
}
