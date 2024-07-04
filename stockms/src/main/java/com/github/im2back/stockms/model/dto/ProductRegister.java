package com.github.im2back.stockms.model.dto;

import java.math.BigDecimal;

public record ProductRegister(
		String name,
		BigDecimal price,
		String code,
		Integer quantity
		) {

}
