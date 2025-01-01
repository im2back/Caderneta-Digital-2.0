package com.github.im2back.stockms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.stockms.model.dto.inputdata.NewProductToSaveDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.model.dto.outputdata.StockUpdateAfterPurchaseResponseDTO;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;

	@Transactional(readOnly = true)
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
	public ProductDTO saveNewProduct(NewProductToSaveDTO p) {
			Product product = repository.save(new Product(p));
			return new ProductDTO(product);	
	}

	@Transactional
	public void deleProductById(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public List<StockUpdateAfterPurchaseResponseDTO> updateQuantityProductsAfterPurchase(List<PurchasedItemDTO> dto) {
			
		List<Product> products = findByCodes(dto);
		List<Product> listOfProductsThatHaveBeenUpdated = persistChangesInTheQuantityOfProducts(dto, products);
		List<StockUpdateAfterPurchaseResponseDTO> response = buildUpdateResponse(dto, listOfProductsThatHaveBeenUpdated);
		
		return  response;
	}

	private List<Product> persistChangesInTheQuantityOfProducts(List<PurchasedItemDTO> productsList,List<Product> products) {
		HashMap<String, Product> productHashMap = new HashMap<>(products.stream().collect(Collectors.toMap(Product::getCode, p -> p)));
		
		for (PurchasedItemDTO p : productsList) {
			Product product = productHashMap.get(p.code());
			product.setQuantity(product.getQuantity() - p.quantity());
		}
		return repository.saveAll(productHashMap.values());	 
	}
	
	private List<StockUpdateAfterPurchaseResponseDTO> buildUpdateResponse(List<PurchasedItemDTO> productsList ,List<Product> products) {
		List<StockUpdateAfterPurchaseResponseDTO> response = new ArrayList<>();
		HashMap<String, Product> productHashMap = new HashMap<>(products.stream().collect(Collectors.toMap(Product::getCode, p -> p)));
		
		productsList.forEach(p -> {
			Product product = productHashMap.get(p.code());
			response.add(new StockUpdateAfterPurchaseResponseDTO(product, p.quantity()));
		});
		return response; 
	}
	
	private List<Product> findByCodes(List<PurchasedItemDTO> productsList) {	
		List<String> productCodesList = new ArrayList<>();
		productsList.forEach(t -> productCodesList.add(t.code()));		
		List<Product> products = repository.findByCodes(productCodesList);
		return products;
	}
	
	//RAFAC : Desfazer uma compra e acionar microsserviço de cliente
	@Transactional
	public void undoIndividualPurchase(UndoPurchaseDTO dto) {
		Product product = findByCode(dto.productCode());
		product.setQuantity(product.getQuantity() + dto.quantity());
		repository.save(product);
		//clientResourceCustomer.undoPurchase(dto);
	}
	
	@Transactional
	public void updateProduct(ProductDTO dto) {
		Product product = findProductById(dto.id());
		product.updateAttributes(dto);
		repository.save(product);
	}
}
