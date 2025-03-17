package com.github.im2back.customerms.validations.customervalidations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CannotBeDuplicatedValidation implements CustomerValidations {

	private final CustomerRepository repository;
	
	@Override
	public void valid(RegisterCustomerDTO requestDto) {
		List<String> errorMessages = new ArrayList<>();
		
		
	    List<Customer> customers = consulta(requestDto);

	        if (!customers.isEmpty()) {
	            for (Customer customer : customers) {
	                if (customer.getEmail().equals(requestDto.email())) {
	                	errorMessages.add("Email Cannot Be Duplicated.");
	                }
	                if (customer.getDocument().equals(requestDto.document())) {
	                	errorMessages.add("Document Cannot Be Duplicated.");
	                }
	                if (customer.getPhone().equals(requestDto.phone())) {
	                	errorMessages.add("Phone Cannot Be Duplicated.");
	                }
	                if (!errorMessages.isEmpty()) {
			        throw new CustomerRegisterValidationException(errorMessages);
			    }
	            }
	        }
	    }

	@Transactional(readOnly = true)
	private List<Customer> consulta(RegisterCustomerDTO requestDto) {
		List<Customer> customers = repository.findByEmailOrDocumentOrPhone(
	           requestDto.email(), requestDto.document(), requestDto.phone()
	        );
		return customers;
	}
	


}
