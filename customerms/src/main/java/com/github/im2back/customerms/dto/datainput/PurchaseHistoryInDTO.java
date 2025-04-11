package com.github.im2back.customerms.dto.datainput;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record PurchaseHistoryInDTO(
		@NotBlank
		String document,	
		List<PurchasedProductsDTO> products
		) {

}
