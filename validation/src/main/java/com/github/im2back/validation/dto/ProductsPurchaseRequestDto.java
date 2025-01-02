package com.github.im2back.validation.dto;

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
