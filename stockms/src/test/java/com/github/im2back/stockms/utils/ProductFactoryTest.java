package com.github.im2back.stockms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.stockms.model.dto.inputdata.NewProductDTO;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItemDTO;
import com.github.im2back.stockms.model.dto.outputdata.ProductDTO;
import com.github.im2back.stockms.model.entities.Product;

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
	
	public static Product createProductAfterPurchaseCode001Quantity5() {
		return new Product(1l, "Nescau", new BigDecimal("19.99").setScale(2, RoundingMode.UP), "001", 5, "ulr");
	}
	
	public static Product createProductAfterPurchaseCode003Quantity5() {
		return new Product(2l, "Arroz", new BigDecimal("10.99").setScale(2, RoundingMode.UP), "003", 5, "ulr");
	}
	
	public static List<Product> createProductListValid() {
		List<Product> productList = new ArrayList<>();
		productList.add(createProductValidCode001Quantity10());
		productList.add(createProductValidCode003Quantity10());
		return productList;
	}
	
	public static List<Product> simulateSavedProductList() {
		List<Product> productList = new ArrayList<>();
		productList.add(createProductAfterPurchaseCode001Quantity5());
		productList.add(createProductAfterPurchaseCode003Quantity5());
		return productList;
	}
	
	public static Product createValidProductWithoutId() {
		return new Product("Nescau", new BigDecimal("19.99").setScale(2, RoundingMode.UP), "001", 10, "ulr");
	}
	
	public static ProductDTO createProductDTOValidCode001Quantity10() {
		Product product = createProductValidCode001Quantity10();
		return new ProductDTO(product);
	}
	
	public static ProductDTO createProductDTOValidCode005Quantity10() {
		Product product = createProductValidCode001Quantity10();
		product.setCode("005");
		return new ProductDTO(product);
	}
	
	public static ProductDTO createProductDTOValidCode001Quantity10ForUpdate() {
		Product product = createProductValidCode001Quantity10();
		product.setName("Nome Atualizado");
		product.setQuantity(100);
		return new ProductDTO(product);
	}
	
	public static NewProductDTO createNewProductToSaveDTOValidCode001Quantity10() {
		Product productValid = createProductValidCode001Quantity10();
		return new NewProductDTO(productValid, 10);
	}
	
	public static NewProductDTO createNewProductToSaveDTOValidCode005Quantity10() {
		Product productValid = createProductValidCode001Quantity10();
		productValid.setCode("005");
		return new NewProductDTO(productValid, 10);
	}
	
	
	public static NewProductDTO createNewProductDTOInvalid() {
		Product productValid = createProductInvalid();
		return new NewProductDTO(productValid, 10);
	}
	
	public static List<PurchasedItemDTO> createListPurchasedItemDTO() {
		PurchasedItemDTO purchasedItemDTO1 = new PurchasedItemDTO("001", 5);
		PurchasedItemDTO purchasedItemDTO2 = new PurchasedItemDTO("003", 5);
		List<PurchasedItemDTO> list = new ArrayList<>();
		list.add(purchasedItemDTO1);
		list.add(purchasedItemDTO2);
		return list;
	}
}
