package com.github.im2back.stockms.model.dto.inputdata;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRegister(

		@NotBlank 
		String name,

		@NotBlank 
		BigDecimal price,

		@NotBlank
		String code,

		@NotNull
		@Positive
		Integer quantity) {

}
