package com.github.im2back.validation.validations.stock;

import java.util.List;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.entities.product.Product;

public interface PurchaseValidationsStock {

	void valid(ProductsPurchaseRequestDto requestDto, List<Product>productsList);
	
}
