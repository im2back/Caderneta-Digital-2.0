package com.github.im2back.customerms.model.dto;

import java.math.BigDecimal;

public record ProductRegister(
		BigDecimal price,
		String name
		) {

}
