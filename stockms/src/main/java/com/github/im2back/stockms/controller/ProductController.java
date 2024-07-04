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

import com.github.im2back.stockms.model.dto.ProductDto;
import com.github.im2back.stockms.model.dto.ProductRegister;
import com.github.im2back.stockms.model.dto.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.service.ProductService;

@RestController
@RequestMapping("product")
public class ProductController { 

	@Autowired
	private ProductService service;

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
		ProductDto response = service.findProductById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<ProductDto> saveNewProduct(@RequestBody ProductRegister product, UriComponentsBuilder uriBuilder) {
		ProductDto response = service.saveNewProduct(product);
		var uri = uriBuilder.path("/product/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
		service.deleProductById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<Void> updateStock(@RequestBody ProductsPurchaseRequestDto dto) {
		service.updateStock(dto);
		return ResponseEntity.ok().build();
	}

}
