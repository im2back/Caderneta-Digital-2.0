package com.github.im2back.customerms.validations.customervalidations;

import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.util.CustomerFactory;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

@ExtendWith(MockitoExtension.class)
class CannotBeDuplicatedValidationTest {

	@InjectMocks
	private CannotBeDuplicatedValidation cannotBeDuplicatedValidation;
	
	@Mock
	private  CustomerRepository repository;
	
	@Test
	void valid_ShouldNotThrowException_WhenValidationPasses() {
		//ARRANGE
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
		List<Customer> list = new ArrayList<>();
		BDDMockito.when(this.repository.findByEmailOrDocumentOrPhone(anyString(), anyString(), anyString())).thenReturn(list);	
		
		//ACT+ ASSERT
		Assertions.assertDoesNotThrow(() -> this.cannotBeDuplicatedValidation.valid(registerCustomerDTO));	
	}
	
	@Test
	void valid_ShouldThrowException_WhenValidationNotPasses() {
		//ARRANGE
		RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
		List<Customer> list = new ArrayList<>();
		list.add(CustomerFactory.createCustomerAndPurchaseEmpity());
		BDDMockito.when(this.repository.findByEmailOrDocumentOrPhone(anyString(), anyString(), anyString())).thenReturn(list);
			
		//ACT+ ASSERT
		Assertions.assertThrows(CustomerRegisterValidationException.class, () ->
			this.cannotBeDuplicatedValidation.valid(registerCustomerDTO));	
	}
}
