package com.github.im2back.customerms.model.dto.datainput;

import com.github.im2back.customerms.model.dto.dataoutput.AddressDto;

import jakarta.validation.constraints.NotBlank;

public record CustomerDto(
		
		@NotBlank
		String name,
		
		@NotBlank
		String document,
		
		@NotBlank
		String email,
		
		@NotBlank
		String phone,
			
		AddressDto address

) {

}
