package com.github.im2back.customerms.model.dto;

import com.github.im2back.customerms.model.entities.customer.Endereco;

public record EnderecoDto(

		String streetName,

		String houseNumber,

		String complement) {
	
	public  EnderecoDto(Endereco e) {
		this(e.getStreetName(),e.getHouseNumber(),e.getComplement());
	}

}
