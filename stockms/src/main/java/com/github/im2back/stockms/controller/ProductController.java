package com.github.im2back.stockms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.stockms.model.dto.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.service.ProductService;

@RestController
@RequestMapping("product")
public class ProductController { 

	@Autowired
	private ProductService service;

	@GetMapping("/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable Long id) {
		var response = service.findProductById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Product> saveNewProduct(@RequestBody Product product, UriComponentsBuilder uriBuilder) {
		var response = service.saveNewProduct(product);
		var uri = uriBuilder.path("/product/{id}").buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
		service.deleProductById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<Product> updateStock(@RequestBody ProductsPurchaseRequestDto dto) {
		service.updateStock(dto);
		return ResponseEntity.ok().build();
	}

}
