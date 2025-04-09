package com.github.im2back.validation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.dto.PurchasedItem;
import com.github.im2back.validation.entities.customer.Customer;
import com.github.im2back.validation.entities.product.Product;
import com.github.im2back.validation.repository.customer.CustomerRepository;
import com.github.im2back.validation.repository.stock.ProductRepository;
import com.github.im2back.validation.validations.customer.PurchaseValidationsCustomer;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;
import com.github.im2back.validation.validations.stock.PurchaseValidationsStock;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidationService {

	private final CustomerRepository customerRepository;
	
	private final ProductRepository productRepository;
	
	private final List<PurchaseValidationsStock> stockPurchaseValidations;
	
	private final List<PurchaseValidationsCustomer> customerPurchaseValidations;
	
	@Transactional(readOnly = true)
	public void validPurchase(ProductsPurchaseRequestDto dto) {
		// Validação de produtos
		List<Product> products = findByCodes(dto.purchasedItems());
		stockPurchaseValidations.forEach(t -> t.valid(dto, products));
			
		// Validação de usuário
		Customer customer = findByCustomerPerDocument(dto.document());
		customerPurchaseValidations.forEach(t -> t.valid(customer));	
	}
	
	protected List<Product> findByCodes(List<PurchasedItem> productsList) {	
		List<String> productCodesList = new ArrayList<>();
		productsList.forEach(t -> productCodesList.add(t.code()));		
		List<Product> products = this.productRepository.findByCodes(productCodesList);
		return products;
	}
	
	
	protected Customer findByCustomerPerDocument(String document) {
		return customerRepository.findByDocument(document)
				.orElseThrow(() -> new PurchaseValidationException(new ArrayList<>(Arrays.asList("User not found for document: " + document))));
	}
}
