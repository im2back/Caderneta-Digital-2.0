package com.github.im2back.stockms.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;

class ProductTest {

	 @Test
	    void updateAttributes_ShouldUpdateOnlyNonNullAndNonBlankFields_WhenProductDTOIsProvided() {
	        Product product = new Product(1l,"Original Name", new BigDecimal("10.00"), "001", 100, "http://original-url.com");
	        ProductDTO dto = new ProductDTO(null, "Updated Name", null, "   ", 50, "http://new-url.com");

	        product.updateAttributes(dto);

	        assertEquals("Updated Name", product.getName());
	        assertEquals(new BigDecimal("10.00"), product.getPrice()); 
	        assertEquals("001", product.getCode()); 
	        assertEquals(50, product.getQuantity());
	        assertEquals("http://new-url.com", product.getProductUrl());
	    }

}
