package com.github.im2back.customerms.dto.dataoutput;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseHistoryOutDTO(
		String customerName,
		List<PurchasedProductDTO> purchasedProducts,
		BigDecimal total
		) {

}
