package com.github.im2back.stockms.model.dto.inputdata;

import jakarta.validation.constraints.NotNull;

public record UndoPurchaseDTO(	
		@NotNull
		Integer quantity
		) {

}
