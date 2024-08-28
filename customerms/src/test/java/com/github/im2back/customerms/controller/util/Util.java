package com.github.im2back.customerms.controller.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.ProductRequestDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.AddressDto;
import com.github.im2back.customerms.model.dto.dataoutput.DailyTotal;
import com.github.im2back.customerms.model.dto.dataoutput.DataForMetricsDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseRecordDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProduct;

public class Util {
	
	public static List<PurchaseRecordDto> purchaseRecordDtoList = new ArrayList<>();
	public static GetCustomerDto getCustomerDto = new GetCustomerDto(1l, "Jefferson",
			"123456789", "jeff@gmail.com", "989144501", new AddressDto("Tv e","06","sem complemento"), 
			purchaseRecordDtoList, new BigDecimal(10));

	public static CustomerDto customerDto = new CustomerDto("Jefferson", "123456789", "jeff@gmail.com", "989144501",
			new AddressDto("Tv e","06","sem complemento"));
	
	public static List<ProductRequestDto> productRequestDtoList = new ArrayList<>();
	public static PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto("123456789", productRequestDtoList);
	
	public static List<PurchasedProduct> PurchasedProduct = new ArrayList<>();
	public static PurchaseResponseDto purchaseResponseDto = new PurchaseResponseDto("jefferson",
			PurchasedProduct, new BigDecimal(100));
	
	public static UndoPurchaseDto undoPurchaseDto = new UndoPurchaseDto(1l, "12345", 10);
	
	 static DailyTotal day1 = new DailyTotal(LocalDate.now().minusDays(6), new BigDecimal("100.50"));
     static DailyTotal day2 = new DailyTotal(LocalDate.now().minusDays(5), new BigDecimal("200.75"));
     static DailyTotal day3 = new DailyTotal(LocalDate.now().minusDays(4), new BigDecimal("150.30"));
     static DailyTotal day4 = new DailyTotal(LocalDate.now().minusDays(3), new BigDecimal("175.00"));
     static DailyTotal day5 = new DailyTotal(LocalDate.now().minusDays(2), new BigDecimal("225.50"));
     static DailyTotal day6 = new DailyTotal(LocalDate.now().minusDays(1), new BigDecimal("300.20"));
     static DailyTotal day7 = new DailyTotal(LocalDate.now(), new BigDecimal("250.00"));

     public static List<DailyTotal> dataGraphicSevenDays = Arrays.asList(day1, day2, day3, day4, day5, day6, day7);

     public static DataForMetricsDto dataForMetrics = new DataForMetricsDto(
             1200.00,  // totalValueForLastMonth
             600.00,   // partialValueForCurrentMonth
             250.00,   // partialValueForCurrentDay
             100.00,   // totalOutstandingAmount
             dataGraphicSevenDays
     );
}


