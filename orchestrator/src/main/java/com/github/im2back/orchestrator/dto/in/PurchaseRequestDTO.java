package com.github.im2back.orchestrator.dto.in;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PurchaseRequestDTO(
		
		@NotBlank
		String document,
		
		@Valid
		@NotEmpty
		List<PurchasedItemDTO> purchasedItems
		) {

}
