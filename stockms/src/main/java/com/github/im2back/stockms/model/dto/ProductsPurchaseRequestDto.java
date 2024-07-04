package com.github.im2back.stockms.model.dto;

import java.util.List;

public record ProductsPurchaseRequestDto(
		String document,
		List<PurchasedItem> purchasedItems
		) {

}
