package com.github.im2back.stockms.validation.purchasevalidations;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;

public interface PurchaseValidations {

	void valid(ProductsPurchaseRequestDto requestDto);
}
