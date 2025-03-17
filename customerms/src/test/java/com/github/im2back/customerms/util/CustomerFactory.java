package com.github.im2back.customerms.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.AddressDTO;
import com.github.im2back.customerms.model.dto.dataoutput.CustomerDTO;
import com.github.im2back.customerms.model.entities.customer.Address;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;

public class CustomerFactory {

	public static Customer createCustomerAndPurchaseEM_ABERTO() {
		Address address = new Address("Rua", "06", "complemento");
		Customer customer = new Customer(1l, "Carlos", "00769203213", "jeff@gmail.com", "91994051023", true, address,null);
		List<PurchaseRecord> purchaseRecords = new ArrayList<>();
		
		purchaseRecords.add(new PurchaseRecord(1l, "Nescau", new BigDecimal(19.99), "001", Instant.now() , 2, Status.EM_ABERTO, customer));
		purchaseRecords.add(new PurchaseRecord(1l, "Nescau", new BigDecimal(19.99), "001", Instant.now() , 2, Status.PAGO, customer));
		customer.setPurchaseRecord(purchaseRecords);
		return customer;
	}
	
	public static Customer createCustomerAndPurchaseEmpity() {
		Address address = new Address("Rua", "06", "complemento");
		Customer customer = new Customer(1l, "Carlos", "00769203213", "jeff@gmail.com", "91994051023", true, address,null);
		List<PurchaseRecord> purchaseRecords = new ArrayList<>();
		customer.setPurchaseRecord(purchaseRecords);
		return customer;
	}
	
	public static CustomerDTO createCustomerDTO() {
		return new CustomerDTO(createCustomerAndPurchaseEM_ABERTO());
	}
	
	public static RegisterCustomerDTO createRegisterCustomerDTO() {
		Address address = new Address("Rua", "06", "complemento");
		AddressDTO addressDTO = new AddressDTO(address);
		return new RegisterCustomerDTO("Carlos", "00769203213", "jeff@gmail.com", "91994051023", addressDTO);
	}
	
	public static RegisterCustomerDTO createRegisterCustomerDTONotValid() {
		Address address = new Address("Rua", "06", "complemento");
		AddressDTO addressDTO = new AddressDTO(address);
		return new RegisterCustomerDTO("", "", "", "", addressDTO);
	}
	
	public static RegisterCustomerDTO createRegisterCustomerDTONotRegistered() {
		Address address = new Address("Rua", "06", "complemento");
		AddressDTO addressDTO = new AddressDTO(address);
		return new RegisterCustomerDTO("Unregistered", "99826467523", "unregistred@gmail.com", "91222051023", addressDTO);
	}
}
