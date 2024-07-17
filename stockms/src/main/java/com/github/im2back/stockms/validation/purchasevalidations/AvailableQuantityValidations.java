package com.github.im2back.stockms.validation.purchasevalidations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;
import com.github.im2back.stockms.validation.exceptions.PurchaseValidationException;

@Component
public class AvailableQuantityValidations implements PurchaseValidations {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public void valid(ProductsPurchaseRequestDto requestDto) {
	    List<PurchasedItem> productsList = requestDto.purchasedItems();
	    List<String> errorMessages = new ArrayList<>();

	    for (PurchasedItem p : productsList) {
	        Product product = productRepository.findByCode(p.code())
	                .orElseThrow(() -> new ProductNotFoundException("Product Not found for code: " + p.code()));
	        
	        if (product.getQuantity() < p.quantity()) {
	            errorMessages.add("Quantidade do produto " + product.getName() +
	                    " não disponível. Quantidade desejada: " + p.quantity() + ", Quantidade em estoque: " + product.getQuantity());
	        }
	    }

	    if (!errorMessages.isEmpty()) {
	        throw new PurchaseValidationException(errorMessages);
	    }
	}


}
