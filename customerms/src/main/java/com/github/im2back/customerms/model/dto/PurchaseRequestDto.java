package com.github.im2back.customerms.model.dto;

import java.util.List;

public record PurchaseRequestDto(
		String customerDocument,
		List<ProductRequestDto> products
		) {

}
