package com.github.im2back.stockms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.service.ProductService;

import jakarta.validation.Valid;

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
	
	@CrossOrigin(origins = "http://localhost:4200") 
	@GetMapping("/code")
	public ResponseEntity<ProductDto> findProductByCode(@RequestParam String code) {
		Product product = service.findByCode(code);
		return ResponseEntity.ok(new ProductDto(product));
	}

	@PostMapping
	public ResponseEntity<ProductDto> saveNewProduct(@RequestBody @Valid ProductRegister product, UriComponentsBuilder uriBuilder) {
		ProductDto response = service.saveNewProduct(product);
		var uri = uriBuilder.path("/product/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
		service.deleProductById(id);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "http://localhost:4200") 
	@PostMapping("/purchase")
	public ResponseEntity<PurchaseResponseDto> updateStock(@RequestBody @Valid ProductsPurchaseRequestDto dto) {
		PurchaseResponseDto response =  service.updateStock(dto);
		return ResponseEntity.ok(response);
	}
	
	@CrossOrigin(origins = "http://localhost:4200") 
	@PutMapping("/undopurchase")
	public ResponseEntity<Void> undoPurchase(@RequestBody @Valid UndoPurchaseDto dto) {
		service.undoPurchase(dto);
		return ResponseEntity.ok().build();
	}

}
