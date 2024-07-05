package com.github.im2back.customerms.model.dto.dataoutput;

import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;

public record GetCustomerDto(Long id, String name, String document, String email, String phone, AddressDto address,
		List<PurchaseRecordDto> purchaseRecord) {
	
	public GetCustomerDto (Customer c) {
		    this(c.getId(),c.getName(),c.getDocument(),c.getEmail(),c.getPhone(),
				new AddressDto(c.getAddress()),
				assembleList(c.getPurchaseRecord())
				);
	}

	private static List<PurchaseRecordDto> assembleList(List<PurchaseRecord> list) {
		List<PurchaseRecordDto> response = new ArrayList<>();

		for (PurchaseRecord p : list) {
			response.add(new PurchaseRecordDto(p.getProductName(), p.getProductprice(), p.getProductCode(),
					p.getPurchaseDate(), p.getQuantity(), p.getStatus()));
		}
		return response;
	}
}
