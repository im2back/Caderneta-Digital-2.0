package com.github.im2back.orchestrator.dto.in;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseHistoryResponseDTO(String customerName, List<PurchasedProduct> purchasedProducts,
		BigDecimal total) {

}
