package com.github.im2back.customerms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.service.CustomerService;

@RestController
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	private CustomerService service;

	@GetMapping("/{id}")
	ResponseEntity<Customer> findCustomerById(@PathVariable Long id) {
		var response = service.findCustomerById(id);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	ResponseEntity<Customer> saveNewCustomer(@RequestBody Customer customer, UriComponentsBuilder uriBuilder) {
		var response = service.saveNewCustomer(customer);
		var uri = uriBuilder.path("/customer/{id}").buildAndExpand(response.getId()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
		service.deleteCustomerById(id);
		return ResponseEntity.noContent().build();
	}

}
