package com.github.im2back.stockms.model.dto;

import java.util.List;

public record PurchaseRegister(
		String document, 
		List<ProductRegister> products) {

}
