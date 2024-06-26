package com.github.im2back.customerms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.entities.customer.Customer;
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

}
