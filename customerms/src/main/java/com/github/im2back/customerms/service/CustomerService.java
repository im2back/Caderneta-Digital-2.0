package com.github.im2back.customerms.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.ProductRequestDto;
import com.github.im2back.customerms.model.dto.PurchaseRequestDto;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Transactional(readOnly = true)
	public Customer findCustomerById(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Transactional
	public Customer saveNewCustomer(Customer customerParam) {
		Customer customerReturn = repository.save(customerParam);
		return customerReturn;
	}

	@Transactional
	public void deleteCustomerById(Long id) {
		repository.deleteById(id);
	}

	public void purchase(PurchaseRequestDto dtoRequest) {
		// localiza o cliente
		Customer customer = repository.findByDocument(dtoRequest.customerDocument())
				.orElseThrow(() -> new RuntimeException());

		// adiciona os produtos na lista referente ao historico de compras
		Instant instant = Instant.now();
		for (ProductRequestDto p : dtoRequest.products()) {
			customer.getPurchaseRecord()
					.add(new PurchaseRecord(p.name(), p.price(), p.code(), instant, customer));
		}
		
		//persiste o historico
		repository.save(customer);
	}

}
