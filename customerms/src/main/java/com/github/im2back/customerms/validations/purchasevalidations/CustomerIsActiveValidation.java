package com.github.im2back.customerms.validations.purchasevalidations;

import org.springframework.stereotype.Component;

import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.validations.exceptions.PurchaseValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerIsActiveValidation implements PurchaseValidations {

	@Override
	public void valid(PurchaseRequestDto dtoRequest,Customer customer) {
		
		if(!customer.isActive()) {
			throw new PurchaseValidationException("Cancelled purchase. The user is inactive.");
		}
	}

	
	
}
