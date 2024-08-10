package com.github.im2back.stockms.model.dto.inputdata;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductsPurchaseRequestDto(
		
		@NotBlank
		String document,
		
		@Valid
		@NotEmpty
		List<PurchasedItem> purchasedItems
		) {

}
