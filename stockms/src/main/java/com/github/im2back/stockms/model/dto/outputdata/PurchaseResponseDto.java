package com.github.im2back.stockms.model.dto.outputdata;

import java.math.BigDecimal;
import java.util.List;



public record PurchaseResponseDto(
		String customerName,
		List<PurchasedProduct> purchasedProducts,
		BigDecimal total
		) {

}
