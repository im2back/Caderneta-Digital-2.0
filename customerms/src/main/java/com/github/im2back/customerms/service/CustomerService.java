package com.github.im2back.customerms.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.GetCustomerDto;
import com.github.im2back.customerms.model.dto.PurchaseRequestDto;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerById(Long id) {
		Customer customer = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		return new GetCustomerDto(customer);		
	}

	@Transactional
	public GetCustomerDto saveNewCustomer(Customer customerParam) {
		Customer customer = repository.save(customerParam);
		return new GetCustomerDto(customer);
	}

	@Transactional
	public void deleteCustomerById(Long id) {
		repository.deleteById(id);
	}

	public void purchase(PurchaseRequestDto dtoRequest) {
		// localiza o cliente
		Customer customer = repository.findByDocument(dtoRequest.document())
				.orElseThrow(() -> new RuntimeException());

		 // Adiciona os produtos na lista referente ao histórico de compras
	    Instant instant = Instant.now();
	    List<PurchaseRecord> purchaseRecords = dtoRequest.products().stream()
	            .map(p -> new PurchaseRecord(p.name(), p.price(), p.code(), instant,p.quantity(),Status.EM_ABERTO,customer))
	            .collect(Collectors.toList());

	    customer.getPurchaseRecord().addAll(purchaseRecords);
	    
	    // Persiste o histórico
	    repository.save(customer);
	}

}
