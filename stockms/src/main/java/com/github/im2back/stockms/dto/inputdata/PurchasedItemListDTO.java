package com.github.im2back.stockms.dto.inputdata;

import java.util.List;

public record PurchasedItemListDTO(
		List<PurchasedItemDTO> items
		) {

}
