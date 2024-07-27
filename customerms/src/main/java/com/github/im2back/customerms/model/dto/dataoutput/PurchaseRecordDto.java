package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;

import com.github.im2back.customerms.model.entities.purchase.Status;

public record PurchaseRecordDto(
		Long purchaseId,
		
		String productName,

		BigDecimal productPrice,

		String productCode,

		String purchaseDate,

		Integer quantity,

		Status status

) {
	


}
