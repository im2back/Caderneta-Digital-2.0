package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;

public record PurchasedProduct(
		String productName,
		Integer quantity,
		BigDecimal value
		
		) {

}
