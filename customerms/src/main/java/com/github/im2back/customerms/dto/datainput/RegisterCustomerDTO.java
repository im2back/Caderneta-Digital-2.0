package com.github.im2back.customerms.dto.datainput;

import com.github.im2back.customerms.dto.dataoutput.AddressDTO;

import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerDTO(
		
		@NotBlank
		String name,
		
		@NotBlank
		String document,
		
		@NotBlank
		String email,
		
		@NotBlank
		String phone,
			
		AddressDTO address

) {

}
