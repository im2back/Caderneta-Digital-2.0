package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.stockms.infra.ClientResourceCustomer;
import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseRegister;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ClientResourceCustomer clientResourceCustomer;
	
	@Transactional(readOnly = true)
	public ProductDto findProductById(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for id: " + id));
		return new ProductDto(product);
	}
	
	@Transactional(readOnly = true)
	private Product findByCode(PurchasedItem p) {
		Product product = repository.findByCode(p.code())
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for code: " + p.code()));
		return product;
	}
	
	@Transactional
	public ProductDto saveNewProduct(ProductRegister p) {
		Product product = repository.save(new Product(p.name(), p.price(), p.code(), p.quantity()));
		return new ProductDto(product);
	}
	
	@Transactional
	public void deleProductById(Long id) {
		repository.deleteById(id);
	}
	
	@Transactional
	public void updateStock(ProductsPurchaseRequestDto dto) {
		
		List<ProductRegister> listPurchaseHistory = new ArrayList<>();
		List<PurchasedItem> productsList = dto.purchasedItems();

		for (PurchasedItem p : productsList) {
			Product product = findByCode(p);
			product.setQuantity(product.getQuantity() - p.quantity());
			listPurchaseHistory.add(new ProductRegister(product, p.quantity()));
			repository.save(product);
		}

		saveUserPurchaseHistory(listPurchaseHistory, dto.document());
	}

	private void saveUserPurchaseHistory(List<ProductRegister> products, String document) {
		PurchaseRegister purchaseRegister = new PurchaseRegister(document, products);
		ResponseEntity<Void> responseRequest = clientResourceCustomer.purchase(purchaseRegister);
	}
}
