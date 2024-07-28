package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;

import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.utils.Util;

public record ProductDataToPdf(
		String name,
		BigDecimal price,
		Integer quantity,
		String data		
		) {
	
	public ProductDataToPdf(PurchaseRecord p){
		this(p.getProductName(),p.getProductprice(),p.getQuantity(),Util.convertDate(p.getPurchaseDate()));
	}

}
