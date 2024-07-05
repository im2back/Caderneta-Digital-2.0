package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.time.Instant;

import com.github.im2back.customerms.model.entities.purchase.Status;

public record PurchaseRecordDto(

		String productName,

		BigDecimal productprice,

		String productCode,

		Instant purchaseDate,

		Integer quantity,

		Status status

) {

}
