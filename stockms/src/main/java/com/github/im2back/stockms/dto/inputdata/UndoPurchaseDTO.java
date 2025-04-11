package com.github.im2back.stockms.dto.inputdata;

import jakarta.validation.constraints.NotNull;

public record UndoPurchaseDTO(	
		@NotNull
		Integer quantity
		) {

}
