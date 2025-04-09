package com.github.im2back.validation.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.validation.entities.product.Product;


public class ProductFactoryTest {

	public static Product createProductValidCode001Quantity10() {
		return new Product(1l, "Nescau", new BigDecimal("19.99").setScale(2, RoundingMode.UP), "001", 10, "ulr");
	}
	
	public static Product createProductInvalid() {
		return new Product(10l, "", null, "", null, "ulr");
	}
	
	public static Product createProductValidCode003Quantity10() {
		return new Product(2l, "Arroz", new BigDecimal("10.99").setScale(2, RoundingMode.UP), "003", 10, "ulr");
	}
	
	public static List<Product> createProductListValid() {
		List<Product> productList = new ArrayList<>();
		productList.add(createProductValidCode001Quantity10());
		productList.add(createProductValidCode003Quantity10());
		return productList;
	}
	
}
