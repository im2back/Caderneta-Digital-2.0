package com.github.im2back.customerms.model.dto.datainput;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record PurchaseRequestDto(
		@NotBlank
		String document,
		
		
		List<ProductRequestDto> products
		) {

}
