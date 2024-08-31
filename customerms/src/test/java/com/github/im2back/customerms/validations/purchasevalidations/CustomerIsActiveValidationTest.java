package com.github.im2back.customerms.validations.purchasevalidations;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.validations.exceptions.PurchaseValidationException;

@ExtendWith(MockitoExtension.class)
class CustomerIsActiveValidationTest {

	@Mock
	private CustomerRepository repository;

	@InjectMocks
	private CustomerIsActiveValidation customerIsActiveValidation;

	@Test
	@DisplayName("Não deveria permitir que usuários inativos fizessem compras")
	void valid() {

		// arrange
		Customer customer = new Customer(1L, "Jefferson", "123456789", "jeff@gmail.com", "989144501", false, null, null);

		Optional<Customer> customerOptional = Optional.ofNullable(customer);

		BDDMockito.when(this.repository.findByDocument("123456789")).thenReturn(customerOptional);
		PurchaseRequestDto dto = new PurchaseRequestDto("123456789", null);

		// act + assert
		Assertions.assertThrows(PurchaseValidationException.class, ()-> customerIsActiveValidation.valid(dto));
	}

}
