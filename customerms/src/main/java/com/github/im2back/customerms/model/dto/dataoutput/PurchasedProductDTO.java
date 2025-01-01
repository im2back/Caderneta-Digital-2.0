package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;

public record PurchasedProductDTO(
		String productName,
		Integer quantity,
		BigDecimal value
		
		) {

}
