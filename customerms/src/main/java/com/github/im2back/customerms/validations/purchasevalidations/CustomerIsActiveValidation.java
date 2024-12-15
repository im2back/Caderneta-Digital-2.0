package com.github.im2back.customerms.validations.purchasevalidations;

import org.springframework.stereotype.Component;

import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.validations.exceptions.PurchaseValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerIsActiveValidation implements PurchaseValidations {

	private final CustomerRepository repository;
	
	@Override
	public void valid(PurchaseRequestDto dtoRequest) {
		var customer = repository.findByDocument(dtoRequest.document())
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with document: " + dtoRequest.document()));
		
		if(!customer.isActive()) {
			throw new PurchaseValidationException("Cancelled purchase. The user is inactive.");
		}
	}

	
	
}
