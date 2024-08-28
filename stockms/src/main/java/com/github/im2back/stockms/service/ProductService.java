package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.stockms.infra.clients.ClientResourceCustomer;
import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseRegister;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;
import com.github.im2back.stockms.validation.purchasevalidations.PurchaseValidations;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ClientResourceCustomer clientResourceCustomer;
	
	@Autowired
	private List<PurchaseValidations> purchaseValidations;
	
	@Transactional(readOnly = true)
	public ProductDto findProductById(Long id) {
		Product product = repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for id: " + id));
		return new ProductDto(product);
	}
	
	@Transactional(readOnly = true)
	public Product findByCode(String code) {
		Product product = repository.findByCode(code)
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for code: " + code));
		return product;
	}
	
	@Transactional
	public ProductDto saveNewProduct(ProductRegister p) {
		try {
			Product product = repository.save(new Product(p.name(), p.price(), p.code(), p.quantity(),p.productUrl()));
			return new ProductDto(product);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Error when trying to save product to database");
		}
			
	}
	
	@Transactional
	public void deleProductById(Long id) {
		repository.deleteById(id);
	}
	
	@Transactional
	public PurchaseResponseDto updateStock(ProductsPurchaseRequestDto dto) {
		
		purchaseValidations.forEach(t -> t.valid(dto));
		
		List<ProductRegister> listPurchaseHistory = new ArrayList<>();
		List<PurchasedItem> productsList = dto.purchasedItems();

		for (PurchasedItem p : productsList) {
			Product product = findByCode(p.code());
			product.setQuantity(product.getQuantity() - p.quantity());
			listPurchaseHistory.add(new ProductRegister(product, p.quantity()));
			repository.save(product);
		}

		return saveUserPurchaseHistory(listPurchaseHistory, dto.document());
	}

	private PurchaseResponseDto saveUserPurchaseHistory(List<ProductRegister> products, String document) {
		PurchaseRegister purchaseRegister = new PurchaseRegister(document, products);
		ResponseEntity<PurchaseResponseDto> responseRequest = clientResourceCustomer.purchase(purchaseRegister);
		return responseRequest.getBody();
	}
	
	@Transactional
	public void undoPurchase(UndoPurchaseDto dto) {
		//encontra o produto
		Product product =  repository.findByCode(dto.productCode())
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for code: " + dto.productCode()));
		
		//adiciona a quantidade de volta ao banco de dados
		product.setQuantity(product.getQuantity() + dto.quantity());
		
		//salva
		repository.save(product);
		
		clientResourceCustomer.undoPurchase(dto);
	}
}
