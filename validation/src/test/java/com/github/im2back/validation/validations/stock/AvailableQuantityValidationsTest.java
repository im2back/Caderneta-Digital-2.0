package com.github.im2back.validation.validations.stock;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.im2back.validation.dto.ProductsPurchaseRequestDto;
import com.github.im2back.validation.dto.PurchasedItem;
import com.github.im2back.validation.entities.product.Product;
import com.github.im2back.validation.util.ProductFactoryTest;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;

class AvailableQuantityValidationsTest {

	private AvailableQuantityValidations availableQuantityValidations = new AvailableQuantityValidations();
	
	@Test
	@DisplayName("Should not throw stock exceptions when available quantity validations are performed with valid data")
	void valid_shouldNotThrowStockExceptions_whenValidRequestAndProductsAreProvided() {
	    // Arrange: Create a valid product list and a valid purchase request DTO
	    List<Product> products = ProductFactoryTest.createProductListValid();
	    List<PurchasedItem> items = List.of(
	        new PurchasedItem("001", 2),
	        new PurchasedItem("003", 2)
	    );
	    ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
	    
	    // Act + Assert: Verify that available quantity validations do not throw any exceptions
	    Assertions.assertDoesNotThrow(() -> this.availableQuantityValidations.valid(dto, products));
	}
	
	@Test
	@DisplayName("Should not throw stock exceptions when available quantity validations are performed with valid data")
	void valid_shouldThrowStockExceptions_whenValidRequestAndProductsAreProvided() {
	    // Arrange: Create a valid product list and a valid purchase request DTO
	    List<Product> products = ProductFactoryTest.createProductListValid();
	    List<PurchasedItem> items = List.of(
	        new PurchasedItem("001", 20),
	        new PurchasedItem("003", 20)
	    );
	    ProductsPurchaseRequestDto dto = new ProductsPurchaseRequestDto("00769203213", items);
	    
	    // Act + Assert: Verify that available quantity validations do not throw any exceptions
	    Assertions.assertThrows(PurchaseValidationException.class, () -> this.availableQuantityValidations.valid(dto, products));
	}

}
