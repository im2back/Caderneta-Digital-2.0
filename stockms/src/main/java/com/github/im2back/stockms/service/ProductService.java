package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;
	private final ClientResourceCustomer clientResourceCustomer;
	private final List<PurchaseValidations> purchaseValidations;

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
			Product product = repository.save(new Product(p));
			return new ProductDto(product);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Error when trying to save product to database");
		}
	}

	@Transactional
	public void deleProductById(Long id) {
		repository.deleteById(id);
	}

	//REFAC:  recebe a lista de produtos comprados, atualiza o estoque envia para processamento no cliente e aguarda resposta para continuar
	@Transactional
	public PurchaseResponseDto updateQuantityProductsAfterPurchase(ProductsPurchaseRequestDto dto) {
		
		List<Product> products = findByCodes(dto.purchasedItems());
		purchaseValidations.forEach(t -> t.valid(dto,products));  
		
		// deduz do estoque a quantidade comprada e persiste a mudança
		List<ProductRegister> listPurchaseHistory = persistChangesInStockQuantityAndBuildHistory(dto.purchasedItems(), products);
		
		// envia para o microsserviço de cliente processar a compra também e devolve a resposta para o controller
		PurchaseResponseDto response = sendingDataForProcessingByTheCientMicroservice(listPurchaseHistory, dto.document());
		return response;
	}

	private List<ProductRegister> persistChangesInStockQuantityAndBuildHistory(List<PurchasedItem> productsList,List<Product> products) {
		//lista enviada para processada pelo microsserviço de cliente. Dados irão ser persistidos no historico de compra
		List<ProductRegister> listPurchaseHistory = new ArrayList<>();
	
		HashMap<String, Product> productHashMap = new HashMap<>(products.stream().collect(Collectors.toMap(Product::getCode, p -> p)));
		
		for (PurchasedItem p : productsList) {
			Product product = productHashMap.get(p.code());
			product.setQuantity(product.getQuantity() - p.quantity());
			listPurchaseHistory.add(new ProductRegister(product, p.quantity()));
		}
		
		List<Product> productListupdate = new ArrayList<>(productHashMap.values());
		repository.saveAll(productListupdate);
		
		return listPurchaseHistory;
	}

	private PurchaseResponseDto sendingDataForProcessingByTheCientMicroservice(List<ProductRegister> products, String document) {
		PurchaseRegister purchaseRegister = new PurchaseRegister(document, products);
		ResponseEntity<PurchaseResponseDto> responseRequest = clientResourceCustomer.purchase(purchaseRegister);
		return responseRequest.getBody();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	protected List<Product> findByCodes(List<PurchasedItem> productsList) {	
		List<String> productCodesList = new ArrayList<>();
		productsList.forEach(t -> productCodesList.add(t.code()));		
		List<Product> products = repository.findByCodes(productCodesList);
		return products;
	}
	
	//RAFAC : Desfazer uma compra e acionar microsserviço de cliente
	@Transactional
	public void undoIndividualPurchase(UndoPurchaseDto dto) {
		Product product = findByCode(dto.productCode());

		product.setQuantity(product.getQuantity() + dto.quantity());

		repository.save(product);
		clientResourceCustomer.undoPurchase(dto);
	}

	public void updateProduct(ProductDto dto) {
		Product product = findProductById(dto.id());
		product.updateAttributes(dto);
		repository.save(product);
	}
}
