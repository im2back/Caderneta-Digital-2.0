package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;

public record PurchasedProduct(
		String productName,
		Integer quantity,
		BigDecimal value
		) {

}
