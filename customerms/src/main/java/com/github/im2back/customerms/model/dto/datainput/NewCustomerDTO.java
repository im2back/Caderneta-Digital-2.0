package com.github.im2back.customerms.model.dto.datainput;

import com.github.im2back.customerms.model.dto.dataoutput.AddressDTO;

import jakarta.validation.constraints.NotBlank;

public record NewCustomerDTO(
		
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
