package com.github.im2back.stockms.dto.outputdata;

import com.github.im2back.stockms.domain.entities.Product;

import jakarta.validation.constraints.NotBlank;

public record MassiveReplenishmentResponseDTO(		@NotBlank 
		String name,

		Integer quantityReplenished,
		
		Integer currentQuantity
) {
	
	 public MassiveReplenishmentResponseDTO(Product product, Integer quantity) {
		this(product.getName(), quantity,product.getQuantity());
	}
}
