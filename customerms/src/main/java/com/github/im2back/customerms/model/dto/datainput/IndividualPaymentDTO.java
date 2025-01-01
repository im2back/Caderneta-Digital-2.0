package com.github.im2back.customerms.model.dto.datainput;

import jakarta.validation.constraints.NotNull;

public record IndividualPaymentDTO(
		@NotNull
		Long purchaseId	
		) {

}
