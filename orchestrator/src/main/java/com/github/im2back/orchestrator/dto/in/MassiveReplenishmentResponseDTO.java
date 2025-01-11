package com.github.im2back.orchestrator.dto.in;

import jakarta.validation.constraints.NotBlank;

public record MassiveReplenishmentResponseDTO(@NotBlank String name,

		Integer quantityReplenished,

		Integer currentQuantity) {

}
