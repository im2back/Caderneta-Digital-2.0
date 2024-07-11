package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseResponseDto(
		String customerName,
		List<PurchasedProduct> purchasedProducts,
		BigDecimal total
		) {

}
