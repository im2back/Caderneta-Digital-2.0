package com.github.im2back.customerms.model.dto;

import java.math.BigDecimal;

public record ProductRequestDto(
		String name,
		BigDecimal price,
		String code
		) {

}
