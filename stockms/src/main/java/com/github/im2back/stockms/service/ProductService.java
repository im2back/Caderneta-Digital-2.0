package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;
	private final ClientResourceCustomer clientResourceCustomer;
	private final List<PurchaseValidations> purchaseValidations;

	@PersistenceContext
	private EntityManager entityManager;

	public Product findProductById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for id: " + id));
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
			Product product = repository.save(new Product(p.name(), p.price(), p.code(), p.quantity(), p.productUrl()));
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
		List<PurchasedItem> productsList = dto.purchasedItems();
		List<Product> products = findByCodes(productsList);
		purchaseValidations.forEach(t -> t.valid(dto,products));

		List<ProductRegister> listPurchaseHistory = new ArrayList<>();
		
		saveAllProducts(listPurchaseHistory, productsList, products);

		return saveUserPurchaseHistory(listPurchaseHistory, dto.document());
	}

	private void saveAllProducts(List<ProductRegister> listPurchaseHistory, List<PurchasedItem> productsList,
			List<Product> products) {
		for (PurchasedItem p : productsList) {
			products.forEach(product -> {
				if (product.getCode().equals(p.code())) {
					product.setQuantity(product.getQuantity() - p.quantity());
					listPurchaseHistory.add(new ProductRegister(product, p.quantity()));
				}
			});
		}
		repository.saveAll(products);
		 // Sincronizar com o banco imediatamente
        entityManager.flush();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	protected List<Product> findByCodes(List<PurchasedItem> productsList) {
		List<String> productCodesList = new ArrayList<>();
		productsList.forEach(t -> productCodesList.add(t.code()));
		List<Product> products = repository.findByCodes(productCodesList);
		return products;
	}

	private PurchaseResponseDto saveUserPurchaseHistory(List<ProductRegister> products, String document) {
		PurchaseRegister purchaseRegister = new PurchaseRegister(document, products);
		ResponseEntity<PurchaseResponseDto> responseRequest = clientResourceCustomer.purchase(purchaseRegister);
		return responseRequest.getBody();
	}

	@Transactional
	public void undoPurchase(UndoPurchaseDto dto) {
		// encontra o produto
		Product product = repository.findByCode(dto.productCode())
				.orElseThrow(() -> new ProductNotFoundException("Product Not found for code: " + dto.productCode()));

		// adiciona a quantidade de volta ao banco de dados
		product.setQuantity(product.getQuantity() + dto.quantity());

		// salva
		repository.save(product);

		clientResourceCustomer.undoPurchase(dto);
	}

	public void updateProduct(ProductDto dto) {
		Product product = findProductById(dto.id());
		product.updateAttributes(dto);
		repository.save(product);
	}
}
