package com.github.im2back.stockms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	public Product findProductById(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
	}

	public Product saveNewProduct(Product product) {
		Product response = repository.save(product);
		return response;
	}

	public void deleProductById(Long id) {
		repository.deleteById(id);
	}
}
