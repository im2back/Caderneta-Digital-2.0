package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;

public record GetCustomerDto(
		Long id, 
		String name,
		String document,
		String email,
		String phone, 
		AddressDto address,
		List<PurchaseRecordDto> purchaseRecord,
		BigDecimal totalDaConta
		)
		
{
	
	public GetCustomerDto (Customer c) {
		    this(c.getId(),c.getName(),c.getDocument(),c.getEmail(),c.getPhone(),
				new AddressDto(c.getAddress()),
				assembleList(c.getPurchaseRecord()),c.getTotal()
				);
	}

	private static List<PurchaseRecordDto> assembleList(List<PurchaseRecord> list) {
		List<PurchaseRecordDto> response = new ArrayList<>();

		for (PurchaseRecord p : list) {
			response.add(new PurchaseRecordDto(p.getId(),p.getProductName(), p.getProductprice(), p.getProductCode(),
					convertDate(p.getPurchaseDate()), p.getQuantity(), p.getStatus()));
		}
		return response;
	}
	
	private static String convertDate(Instant instant) {

		OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.ofHours(-3));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = offsetDateTime.format(formatter);;
         
        return formattedDate;
	}
	
	
}
