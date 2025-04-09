package com.github.im2back.validation.entities.customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CustomerTest {

	@Test
	void getTotal_ShoudReturnTotalEMABERTO_WhenCustomerValid() {
		//ARRANGE
		Address address = new Address("Rua", "06", "complemento");
		Customer customer = new Customer(1l, "Carlos", "00769203213", "jeff@gmail.com", "91994051023", true, address,null);
		List<PurchaseRecord> purchaseRecords = new ArrayList<>();
		
		purchaseRecords.add(new PurchaseRecord(1l, "Nescau", new BigDecimal(19.99), "001", Instant.now() , 2, Status.EM_ABERTO, customer));
		purchaseRecords.add(new PurchaseRecord(1l, "Nescau", new BigDecimal(19.99), "001", Instant.now() , 2, Status.PAGO, customer));
		customer.setPurchaseRecord(purchaseRecords);
		
		//ACT
		var response = customer.getTotal();
		
		//ASSERT
		Assertions.assertEquals(new BigDecimal(39.98).setScale(0, RoundingMode.HALF_UP), response.setScale(0, RoundingMode.HALF_UP));
	}

}
