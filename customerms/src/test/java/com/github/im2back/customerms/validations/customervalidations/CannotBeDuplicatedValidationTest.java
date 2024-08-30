package com.github.im2back.customerms.validations.customervalidations;

import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

@ExtendWith(MockitoExtension.class)
class CannotBeDuplicatedValidationTest {
	
	@Mock
	private CustomerRepository repository;
	
	@InjectMocks
	private CannotBeDuplicatedValidation cannotBeDuplicatedValidation;
	
	@Test
	@DisplayName("Não deveria permitir o cadastro com documento duplicado")
	void valid() {
		
		//arrange
		CustomerDto customerDto = new CustomerDto("Jefferson", "123456789", "jeff@gmail.com", "989144501",null );
		
		 Customer customer = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
				null, null);
		
		Optional<Customer> customerOptional =  Optional.ofNullable(customer); 
		
		BDDMockito.when(repository.findByDocument(anyString())).thenReturn(customerOptional);
		
		//act + assert
		
		   Assertions.assertThrows(CustomerRegisterValidationException.class, () -> {
		        cannotBeDuplicatedValidation.valid(customerDto);
		    });
	}
	
	@Test
	@DisplayName("Não deveria permitir o cadastro com documento duplicado")
	void validCenario02() {
		
		//arrange
		CustomerDto customerDto = new CustomerDto("Jefferson", "123456789", "jeff@gmail.com", "989144501",null );
		
		 Customer customer = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
				null, null);
		
		Optional<Customer> customerOptional =  Optional.ofNullable(customer); 
		
		BDDMockito.when(repository.findByEmail(anyString())).thenReturn(customerOptional);
		
		//act + assert
		
		   Assertions.assertThrows(CustomerRegisterValidationException.class, () -> {
		        cannotBeDuplicatedValidation.valid(customerDto);
		    });
	}
	
	@Test
	@DisplayName("Não deveria permitir o cadastro com documento duplicado")
	void validCenario03() {
		
		//arrange
		CustomerDto customerDto = new CustomerDto("Jefferson", "123456789", "jeff@gmail.com", "989144501",null );
		
		 Customer customer = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", true,
				null, null);
		
		Optional<Customer> customerOptional =  Optional.ofNullable(customer); 
		
		BDDMockito.when(repository.findByPhone(anyString())).thenReturn(customerOptional);
		
		//act + assert
		
		   Assertions.assertThrows(CustomerRegisterValidationException.class, () -> {
		        cannotBeDuplicatedValidation.valid(customerDto);
		    });
	}

}
