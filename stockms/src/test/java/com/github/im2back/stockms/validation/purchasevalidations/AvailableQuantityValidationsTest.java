package com.github.im2back.stockms.validation.purchasevalidations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.repositories.ProductRepository;
import com.github.im2back.stockms.service.exceptions.ProductNotFoundException;
import com.github.im2back.stockms.validation.exceptions.PurchaseValidationException;

@ExtendWith(MockitoExtension.class)
class AvailableQuantityValidationsTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private AvailableQuantityValidations availableQuantityValidations;

	@Test
	@DisplayName("Não deveria lançar exceções para produtos e quantidades validas")
	void validCenario01() {
		// arrange
		List<PurchasedItem> purchasedItems = new ArrayList<>();
		PurchasedItem item = new PurchasedItem("001", 10);
		purchasedItems.add(item);
		ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
				purchasedItems);

		Product product = new Product(1l, "Arroz", new BigDecimal(100), "001", 100, "www.url.com");
		Optional<Product> productOptional = Optional.ofNullable(product);
		BDDMockito.when(productRepository.findByCode("001")).thenReturn(productOptional);
			
		// assert + act
		Assertions.assertDoesNotThrow(() -> availableQuantityValidations.valid(productsPurchaseRequestDto));
	}
	
	@Test
	@DisplayName("deveria lançar exceções ProductNotFoundException para produtos não encontrados")
	void validCenario02() {
		// arrange
		List<PurchasedItem> purchasedItems = new ArrayList<>();
		PurchasedItem item = new PurchasedItem("001", 10);
		purchasedItems.add(item);
		ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
				purchasedItems);

		Optional<Product> productOptional = Optional.ofNullable(null);
		BDDMockito.when(productRepository.findByCode("001")).thenReturn(productOptional);
			
		// assert + act
		Assertions.assertThrows(ProductNotFoundException.class,() -> availableQuantityValidations.valid(productsPurchaseRequestDto));
	}
	
	@Test
	@DisplayName("deveria lançar exceção PurchaseValidationException em caso de validação de quantidade insuficiente no estoque")
	void validCenario03() {
		// arrange
		List<PurchasedItem> purchasedItems = new ArrayList<>();
		PurchasedItem item = new PurchasedItem("001", 200);
		purchasedItems.add(item);
		ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
				purchasedItems);

		Product product = new Product(1l, "Arroz", new BigDecimal(100), "001", 100, "www.url.com");
		Optional<Product> productOptional = Optional.ofNullable(product);
		BDDMockito.when(productRepository.findByCode("001")).thenReturn(productOptional);
			
		// assert + act
		Assertions.assertThrows(PurchaseValidationException.class,() -> availableQuantityValidations.valid(productsPurchaseRequestDto));
	}


}
