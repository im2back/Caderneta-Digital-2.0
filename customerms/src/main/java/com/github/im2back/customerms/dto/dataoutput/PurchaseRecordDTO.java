package com.github.im2back.customerms.dto.dataoutput;

import java.math.BigDecimal;

import com.github.im2back.customerms.domain.enums.Status;

public record PurchaseRecordDTO(
		Long purchaseId,
		
		String productName,

		BigDecimal productPrice,

		String productCode,

		String purchaseDate,

		Integer quantity,

		Status status

) {
	


}
