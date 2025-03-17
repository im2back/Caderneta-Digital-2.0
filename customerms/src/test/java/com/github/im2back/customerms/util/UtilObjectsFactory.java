package com.github.im2back.customerms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.datainput.PurchasedProductsDTO;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseHistoryOutDTO;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProductDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DailyTotalDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DataForMetricsDTO;

public class UtilObjectsFactory {
	
	public static DataForMetricsDTO createDataForMetricsDTO() {
		List<DailyTotalDTO> dataGraphicSevenDays = new ArrayList<>();
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(15.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(11.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(12.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(13.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(14.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(16.00).setScale(2, RoundingMode.HALF_UP)));
		dataGraphicSevenDays.add(new DailyTotalDTO(LocalDate.now(), new BigDecimal(17.00).setScale(2, RoundingMode.HALF_UP)));
		
		return new DataForMetricsDTO(
				 100.00, 
				 70.00,
				 5.00,
				 40.00,
				 dataGraphicSevenDays);
	}

	public static PurchaseHistoryInDTO createPurchaseHistoryInDTO() {
		List<PurchasedProductsDTO> productsDTOs = new ArrayList<>();
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		return new PurchaseHistoryInDTO("00769203213", productsDTOs);
	}
	
	public static PurchaseHistoryInDTO createPurchaseHistoryInDTODocumentNotValid() {
		List<PurchasedProductsDTO> productsDTOs = new ArrayList<>();
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		return new PurchaseHistoryInDTO("9999999999", productsDTOs);
	}
	
	public static PurchaseHistoryInDTO createPurchaseHistoryInDTONotValid() {
		List<PurchasedProductsDTO> productsDTOs = new ArrayList<>();
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		productsDTOs.add(new PurchasedProductsDTO("Nescau",  new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP), "001", 10));
		return new PurchaseHistoryInDTO("", productsDTOs);
	}
	
	public static PurchaseHistoryOutDTO createPurchaseHistoryOutDTO() {
		List<PurchasedProductDTO> purchasedProducts = new ArrayList<>();
		purchasedProducts.add(new PurchasedProductDTO("Nescau", 10, new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP)));
		purchasedProducts.add(new PurchasedProductDTO("Nescau", 10, new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP)));
		purchasedProducts.add(new PurchasedProductDTO("Nescau", 10, new BigDecimal(19.99).setScale(2, RoundingMode.HALF_UP)));
		return new PurchaseHistoryOutDTO("Jefferson", purchasedProducts, new BigDecimal(599.7).setScale(2, RoundingMode.HALF_UP));
	}
}
