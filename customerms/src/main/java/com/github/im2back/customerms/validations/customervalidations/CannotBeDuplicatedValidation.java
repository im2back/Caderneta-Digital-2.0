package com.github.im2back.customerms.validations.customervalidations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CannotBeDuplicatedValidation implements CustomerValidations {

	private final CustomerRepository repository;
	
	@Override
	public void valid(CustomerDto requestDto) {
		 	List<String> errorMessages = new ArrayList<>();
			var customerEmail = repository.findByEmail(requestDto.email());
			var customerDocument = repository.findByDocument(requestDto.document());
			var customerPhone = repository.findByPhone(requestDto.phone());

			if(customerEmail.isPresent()) {
				errorMessages.add("Email Cannot Be Duplicated.");
			}
			
			if(customerDocument.isPresent()) {
				errorMessages.add("Document Cannot Be Duplicated.");
			}
			
			if(customerPhone.isPresent()) {
				errorMessages.add("Phone Cannot Be Duplicated.");
			}
			
			 if (!errorMessages.isEmpty()) {
			        throw new CustomerRegisterValidationException(errorMessages);
			    }
	}

}
