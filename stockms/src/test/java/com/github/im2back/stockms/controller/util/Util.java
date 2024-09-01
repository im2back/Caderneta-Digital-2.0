package com.github.im2back.stockms.controller.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.stockms.model.dto.inputdata.ProductRegister;
import com.github.im2back.stockms.model.dto.inputdata.ProductsPurchaseRequestDto;
import com.github.im2back.stockms.model.dto.inputdata.PurchasedItem;
import com.github.im2back.stockms.model.dto.inputdata.UndoPurchaseDto;
import com.github.im2back.stockms.model.dto.outputdata.ProductDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchaseResponseDto;
import com.github.im2back.stockms.model.dto.outputdata.PurchasedProduct;
import com.github.im2back.stockms.model.entities.Product;

public class Util {
	
	public static Product product = new Product(1l, "Arroz", new BigDecimal(100), "001", 100, "www.url.com");
	
	public static ProductDto ProductDto = new ProductDto(product);
	
	public static ProductRegister productRegister = new ProductRegister(product, 100);
	
	public static List<PurchasedItem> purchasedItems = new ArrayList<>();
	public static ProductsPurchaseRequestDto productsPurchaseRequestDto = new ProductsPurchaseRequestDto("123456789",
			purchasedItems);
	static {
		PurchasedItem item = new PurchasedItem("001", 10);
		purchasedItems.add(item);
	}
	
	public static List<PurchasedProduct> purchasedProduct = new ArrayList<>();
	public static PurchaseResponseDto purchaseResponseDto = new PurchaseResponseDto("jeff", purchasedProduct, new BigDecimal(1000));
	
	static {
		PurchasedProduct p = new PurchasedProduct("Arroz", 10, new BigDecimal(100));
		purchasedProduct.add(p);
	}
	
	public static UndoPurchaseDto undoPurchaseDto = new UndoPurchaseDto(1l, "001", 3);
}
