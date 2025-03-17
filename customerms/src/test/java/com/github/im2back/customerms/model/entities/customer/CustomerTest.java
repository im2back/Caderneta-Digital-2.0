package com.github.im2back.customerms.model.entities.customer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.im2back.customerms.util.CustomerFactory;

class CustomerTest {

	@Test
	void getTotal_ShoudReturnTotalEMABERTO_WhenCustomerValid() {
		//ARRANGE
		Customer customer = CustomerFactory.createCustomerAndPurchaseEM_ABERTO();
		
		//ACT
		var response = customer.getTotal();
		
		//ASSERT
		Assertions.assertEquals(new BigDecimal(39.98).setScale(0, RoundingMode.HALF_UP), response.setScale(0, RoundingMode.HALF_UP));
	}
}
