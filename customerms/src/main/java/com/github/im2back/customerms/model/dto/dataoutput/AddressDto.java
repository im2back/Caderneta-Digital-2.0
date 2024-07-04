package com.github.im2back.customerms.model.dto.dataoutput;

import com.github.im2back.customerms.model.entities.customer.Address;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
		
		@NotBlank
		String streetName,
		
		@NotBlank
		String houseNumber,
		
		@NotBlank
		String complement) {
	
	public  AddressDto(Address e) {
		this(e.getStreetName(),e.getHouseNumber(),e.getComplement());
	}

}
