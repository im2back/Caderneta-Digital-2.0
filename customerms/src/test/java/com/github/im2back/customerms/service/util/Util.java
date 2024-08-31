package com.github.im2back.customerms.service.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.ProductRequestDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.AddressDto;
import com.github.im2back.customerms.model.entities.customer.Address;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;

public class Util {

	public static Address adress = new Address("Travessa E", "06", "Complemento");
	public static AddressDto addressDto = new AddressDto(adress);

	public static List<PurchaseRecord> purchaseRecordList = new ArrayList<>();

	public static PurchaseRecord purchaseRecord1 = new PurchaseRecord(1l, "Arroz", new BigDecimal(10), "12345",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord2 = new PurchaseRecord(2l, "Feijao", new BigDecimal(10), "123456",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord3 = new PurchaseRecord(3l, "Frango", new BigDecimal(10), "1234567",
			Instant.now(), 1, Status.PAGO, null);
	

	static {
		
		purchaseRecordList.add(purchaseRecord2);
		purchaseRecordList.add(purchaseRecord1);
		purchaseRecordList.add(purchaseRecord3);
	}

	public static Customer customer = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
			adress, purchaseRecordList);
	
	public static List<PurchaseRecord> purchaseRecordListVazia = new ArrayList<>();
	public static Customer customerListaVazia = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
			adress, purchaseRecordListVazia);
	
	public static Optional<Customer> customerListaVaziaOptional = Optional.ofNullable(customerListaVazia);
	public static Optional<Customer> customerOptional = Optional.ofNullable(customer);
	
	public static CustomerDto customerDto = new CustomerDto("Jefferson", "123456789", "jeff@gmail.com", "989144501",addressDto );
	
	public static List<ProductRequestDto> productRequestDtoList = new ArrayList<>();
	public static ProductRequestDto product1 = new ProductRequestDto("Produto A", new BigDecimal("99.99"), "A001", 10);
	public static ProductRequestDto product2 = new ProductRequestDto("Produto B", new BigDecimal("49.99"), "B002", 5);
     
     static {
    	 productRequestDtoList.add(product1);
    	 productRequestDtoList.add(product2);
     }
	public static PurchaseRequestDto purchaseRequestDto = new PurchaseRequestDto("123456789", productRequestDtoList);
	
	public static UndoPurchaseDto undoPurchaseDto = new UndoPurchaseDto(1l, "12345", 1);

	 
	// Exclusivo do metodo generatePurchaseInvoice
	public static List<PurchaseRecord> purchaseRecordList2 = new ArrayList<>();

	public static PurchaseRecord purchaseRecord4 = new PurchaseRecord(1l, "Arroz", new BigDecimal(10), "12345",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord5 = new PurchaseRecord(2l, "Feijao", new BigDecimal(10), "123456",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord6 = new PurchaseRecord(3l, "Frango", new BigDecimal(10), "1234567",
			Instant.now(), 1, Status.PAGO, null);
	

	static {
		
		purchaseRecordList2.add(purchaseRecord4);
		purchaseRecordList2.add(purchaseRecord5);
		purchaseRecordList2.add(purchaseRecord6);
	}

	public static Customer customer2 = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
			adress, purchaseRecordList2);
	
	public static Optional<Customer> customerOptional2 = Optional.ofNullable(customer2);
	
	
	// Exclusivo do metodo individualPayment
	public static List<PurchaseRecord> purchaseRecordList3 = new ArrayList<>();

	public static PurchaseRecord purchaseRecord7 = new PurchaseRecord(1l, "Arroz", new BigDecimal(10), "12345",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord8 = new PurchaseRecord(2l, "Feijao", new BigDecimal(10), "123456",
			Instant.now(), 1, Status.EM_ABERTO, null);
	
	public static PurchaseRecord purchaseRecord9 = new PurchaseRecord(3l, "Frango", new BigDecimal(10), "1234567",
			Instant.now(), 1, Status.PAGO, null);
	

	static {
		
		purchaseRecordList3.add(purchaseRecord7);
		purchaseRecordList3.add(purchaseRecord8);
		purchaseRecordList3.add(purchaseRecord9);
	}

	public static Customer customer3 = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
			adress, purchaseRecordList3);
	
	public static Optional<Customer> customerOptional3 = Optional.ofNullable(customer3);
}
