package com.github.im2back.stockms.model.dto.outputdata;

import java.util.List;

import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;

public record PurchaseRegister(
		String document, 
		List<ProductRegister> products) {

}
