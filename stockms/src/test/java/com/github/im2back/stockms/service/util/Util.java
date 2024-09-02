package com.github.im2back.stockms.service.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseRegister;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchasedProduct;
import com.github.im2back.stockms.model.entities.Product;

public class Util {

	public static Product product = new Product(1l, "Arroz", new BigDecimal(100), "001", 100, "www.url.com");
	
	public static Optional<Product> productOptional = Optional.ofNullable(product);
	
	public static ProductDto ProductDto = new ProductDto(product);
	
	public static ProductRegister productRegister = new ProductRegister(product, 100);
	
	public static List<PurchasedItem> purchasedItems = new ArrayList<>();
	public static ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
			purchasedItems);
	static {
		PurchasedItem item = new PurchasedItem("001", 10);
		purchasedItems.add(item);
	}
	
	public static ProductRegister productRegister2 = new ProductRegister(product, 10);
	public static List<ProductRegister> productRegisterList = new ArrayList<>();
	
	static {
		productRegisterList.add(productRegister2);
	}
	
	public static List<PurchasedProduct>  purchasedProductList = new ArrayList<>();
	public static PurchaseResponseDto PurchaseResponseDto = new PurchaseResponseDto("Jeff", purchasedProductList, new BigDecimal(1000));
	
	
	static {
		PurchasedProduct PurchasedProduct = new PurchasedProduct("Arroz", 10, new BigDecimal(100));
		purchasedProductList.add(PurchasedProduct);
	}
	
	public static PurchaseRegister  purchaseRegister = new PurchaseRegister("123456789", productRegisterList);
	
	public static Product productSave = new Product(1l, "Arroz", new BigDecimal(100), "001", 90, "www.url.com");
	
	public static Product productUndoPurchase = new Product(1l, "Arroz", new BigDecimal(100), "001", 9, "www.url.com");
	
	public static UndoPurchaseDto undoPurchaseDto = new UndoPurchaseDto(1l, "001", 1);

	
	public static Optional<Product> productUndoPurchaseOptional = Optional.ofNullable(productUndoPurchase);
	
}
