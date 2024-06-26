package com.github.im2back.customerms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;
	

	public Customer findCustomerById(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public Customer saveNewCustomer(Customer customerParam) {
		Customer customerReturn = repository.save(customerParam);
		return customerReturn;
	}
	
	public void deleteCustomerById(Long id) {
		repository.deleteById(id);
	}

}
