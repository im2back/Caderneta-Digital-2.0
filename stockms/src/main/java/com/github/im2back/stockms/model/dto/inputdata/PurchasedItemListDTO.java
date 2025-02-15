package com.github.im2back.stockms.model.dto.inputdata;

import java.util.List;

public record PurchasedItemListDTO(
		List<PurchasedItemDTO> items
		) {

}
