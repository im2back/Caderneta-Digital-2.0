package com.github.im2back.customerms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	private CustomerService service;

	@GetMapping("/{id}")
	ResponseEntity<GetCustomerDto> findCustomerById(@PathVariable Long id) {
		GetCustomerDto response = service.findCustomerById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/findDocument")
	ResponseEntity<GetCustomerDto> findCustomerByDocument(@RequestParam String document) {
		GetCustomerDto response = service.findCustomerByDocument(document);
		return ResponseEntity.ok(response);
	}
	
		
	@PostMapping
	ResponseEntity<GetCustomerDto> saveNewCustomer(@RequestBody @Valid CustomerDto customer,
			UriComponentsBuilder uriBuilder) {
		GetCustomerDto response = service.saveNewCustomer(customer);
		var uri = uriBuilder.path("/customer/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
		service.deleteCustomerById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping
	ResponseEntity<PurchaseResponseDto> purchase(@RequestBody @Valid PurchaseRequestDto dtoRequest) {
		PurchaseResponseDto response = service.purchase(dtoRequest);
		return ResponseEntity.ok(response);
	}

}
