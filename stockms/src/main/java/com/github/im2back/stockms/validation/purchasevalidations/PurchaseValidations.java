package com.github.im2back.stockms.validation.purchasevalidations;

import java.util.List;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.entities.Product;

public interface PurchaseValidations {

	void valid(ProductsPurchaseRequestDto requestDto,List<Product>productsList);
}
