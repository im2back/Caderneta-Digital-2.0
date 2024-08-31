package com.github.im2back.customerms.model.entities.customer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;

class CustomerTest {

	@Test
	void test() {
		// ARRANGE 
		Customer customer = new Customer(1l, "Carlos", "123456789", "jeff@gmail.com", "989144501", true, null, null);
		
		PurchaseRecord purchaseRecord1 = new PurchaseRecord(1l, "Arroz", new BigDecimal(7),"001", 
				Instant.now(), 1, Status.EM_ABERTO, customer);
		PurchaseRecord purchaseRecord2 = new PurchaseRecord(2l, "Feijao", new BigDecimal(10),"002", 
				Instant.now(), 2, Status.EM_ABERTO, customer);
		
		List<PurchaseRecord> list = new ArrayList<>();
		list.add(purchaseRecord1);
		list.add(purchaseRecord2);
		
		customer.setPurchaseRecord(list);
		
		// ACT
		customer.getTotal();
		
		// ASSERT
		Assertions.assertEquals(new BigDecimal(27), customer.getTotal(),"deveria multiplicar quantidade x preço para cada"
				+ "item da lista e no final somar tudo");
	}

}
