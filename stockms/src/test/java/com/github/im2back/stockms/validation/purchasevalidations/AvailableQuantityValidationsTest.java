package com.github.im2back.stockms.validation.purchasevalidations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.entities.Product;
import com.github.im2back.stockms.validation.exceptions.PurchaseValidationException;


class AvailableQuantityValidationsTest {
	
	
	 private AvailableQuantityValidations availableQuantityValidations = new AvailableQuantityValidations();

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
		List<Product> productsList= new ArrayList<>();
		productsList.add(product);
			
		// assert + act
		Assertions.assertDoesNotThrow(() -> availableQuantityValidations.valid(productsPurchaseRequestDto,productsList));
	}
	
	@Test
	@DisplayName("deveria lançar exceções ProductNotFoundException para produtos não encontrados")
	void validCenario02() {
		//ProductsPurchaseRequestDto requestDto,List<Product> produtosEncontrados
		// arrange
		List<PurchasedItem> purchasedItems = new ArrayList<>();
		PurchasedItem item = new PurchasedItem("001", 10);
		purchasedItems.add(item);
		ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
				purchasedItems);
		List<Product> productsList= new ArrayList<>();
			
		// assert + act
		Assertions.assertThrows(PurchaseValidationException.class,() -> availableQuantityValidations.valid(productsPurchaseRequestDto,productsList));
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
		List<Product> productsList = new ArrayList<>();
		productsList.add(product);
			
		// assert + act
		Assertions.assertThrows(PurchaseValidationException.class,() -> availableQuantityValidations.valid(productsPurchaseRequestDto,productsList));
	}

}
