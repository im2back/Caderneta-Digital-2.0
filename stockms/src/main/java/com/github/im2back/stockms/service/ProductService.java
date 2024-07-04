package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.im2back.stockms.infra.ClientResourceCustomer;
import com.github.im2back.stockms.model.dto.ProductDto;
import com.github.im2back.stockms.model.dto.ProductRegister;
import com.github.im2back.stockms.model.dto.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.PurchaseRegister;
import com.github.im2back.stockms.model.dto.PurchasedItem;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ClientResourceCustomer clientResourceCustomer;

	public ProductDto findProductById(Long id) {
		 Product product = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
		 return new ProductDto(product);
	}

	public ProductDto saveNewProduct(ProductRegister p) {
		Product product = repository.save(new Product( p.name(), p.price(), p.code(), p.quantity()));
		 return new ProductDto(product);
	}

	public void deleProductById(Long id) {
		repository.deleteById(id);
	}

	public void updateStock(ProductsPurchaseRequestDto dto) {
		List<ProductRegister> listProductRegister = new ArrayList<>();

		List<PurchasedItem> productsList = dto.purchasedItems();
		for (PurchasedItem p : productsList) {
			Product product = repository.findByCode(p.code()).orElseThrow(() -> new RuntimeException("Not found"));
			product.setQuantity(product.getQuantity() - p.quantity());
			listProductRegister
					.add(new ProductRegister(product.getName(), product.getPrice(), product.getCode(), p.quantity()));
			repository.save(product);
		}

		saveHistory(listProductRegister, dto.document());
	}

	private void saveHistory(List<ProductRegister> products, String document) {
		PurchaseRegister purchaseRegister = new PurchaseRegister(document, products);
		clientResourceCustomer.purchase(purchaseRegister);
	}
}
