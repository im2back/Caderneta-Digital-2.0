package com.github.im2back.validation.validations.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.im2back.validation.entities.customer.Address;
import com.github.im2back.validation.entities.customer.Customer;
import com.github.im2back.validation.validations.exceptions.PurchaseValidationException;

class CustomerIsActiveValidationTest {

	private CustomerIsActiveValidation customerIsActiveValidation = new CustomerIsActiveValidation();
	
	@Test
	@DisplayName("Should not throw an exception when the user is active")
	void customerIsActiveValidation_shouldNotThrowException_whenUserIsActive() {
	    // ARRANGE
	    Address address = new Address("nome da rua", "06", "complemento");
	    Customer customer = new Customer("Jefferson Souza", "00769203213", "jeff@gmail.com", "989144501", true, address);
	    
	    // ACT + ASSERT
	    Assertions.assertDoesNotThrow(() -> this.customerIsActiveValidation.valid(customer));
	}

	@Test
	@DisplayName("Should throw PurchaseValidationException when the user is inactive")
	void customerIsActiveValidation_shouldThrowPurchaseValidationException_whenUserIsInactive() {
	    // ARRANGE
	    Address address = new Address("nome da rua", "06", "complemento");
	    Customer customer = new Customer("Jefferson Souza", "00769203213", "jeff@gmail.com", "989144501", false, address);
	    
	    // ACT + ASSERT
	    Assertions.assertThrows(PurchaseValidationException.class, () -> this.customerIsActiveValidation.valid(customer));
	}


}
