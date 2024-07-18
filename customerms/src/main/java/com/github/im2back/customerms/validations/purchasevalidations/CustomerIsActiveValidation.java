package com.github.im2back.customerms.validations.purchasevalidations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.validations.exceptions.PurchaseValidationException;

@Component
public class CustomerIsActiveValidation implements PurchaseValidations {

	@Autowired
	private CustomerRepository repository;
	
	@Override
	public void valid(PurchaseRequestDto dtoRequest) {
		var customer = repository.findByDocument(dtoRequest.document()).get();
		
		if(!customer.isActive()) {
			throw new PurchaseValidationException("Cancelled purchase. The user is inactive.");
		}
	}

	
	
}
