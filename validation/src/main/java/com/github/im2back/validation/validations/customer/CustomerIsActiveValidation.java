package com.github.im2back.validation.validations.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.im2back.validation.entities.customer.Customer;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerIsActiveValidation implements PurchaseValidationsCustomer {

	@Override
	public void valid(Customer customer) {
		List<String> errorMessages = new ArrayList<>();
		if (customer.isActive() == false) {
			errorMessages.add("Cancelled purchase. The user is inactive.");
		}
		if (!errorMessages.isEmpty()) {
			throw new PurchaseValidationException(errorMessages);
		}
	}

}
