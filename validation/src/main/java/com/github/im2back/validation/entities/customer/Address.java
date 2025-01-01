package com.github.im2back.validation.entities.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Column(name = "street_name")
	private String streetName;

	@Column(name = "house_number" )
	private String houseNumber;

	private String complement;

}
