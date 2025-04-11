package com.github.im2back.customerms.dto.dataoutput;

import com.github.im2back.customerms.domain.entities.customer.Address;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
		
		@NotBlank
		String streetName,
		
		@NotBlank
		String houseNumber,
		
		@NotBlank
		String complement) {
	
	public  AddressDTO(Address e) {
		this(e.getStreetName(),e.getHouseNumber(),e.getComplement());
	}

}
