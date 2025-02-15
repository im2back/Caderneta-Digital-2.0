package com.github.im2back.orchestrator.dto.in;

import java.math.BigDecimal;

public record PurchasedProductDTO(

		String productName,
		Integer quantity,
		BigDecimal value
		) {

}
