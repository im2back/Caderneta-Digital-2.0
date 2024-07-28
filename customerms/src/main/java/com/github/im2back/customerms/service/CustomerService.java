package com.github.im2back.customerms.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProduct;
import com.github.im2back.customerms.model.entities.customer.Address;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.validations.customervalidations.CustomerValidations;
import com.github.im2back.customerms.validations.purchasevalidations.PurchaseValidations;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private List<CustomerValidations> customerValidations;

	@Autowired
	private List<PurchaseValidations> purchaseValidations;

	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerById(Long id) {
		Customer customer = repository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for id: " + id));
		return new GetCustomerDto(customer);
	}
	
	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerByDocument(String document) {
		Customer customer = repository.findByDocument(document)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for document: " + document));
		return new GetCustomerDto(customer);
	}

	@Transactional
	public GetCustomerDto saveNewCustomer(CustomerDto dto) {
		customerValidations.forEach(t -> t.valid(dto));

		Customer customer = repository.save(new Customer(dto.name(), dto.document(), dto.email(), dto.phone(), true,
				new Address(dto.address().streetName(), dto.address().houseNumber(), dto.address().complement())));
		return new GetCustomerDto(customer);
	}

	@Transactional
	public void deleteCustomerById(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public PurchaseResponseDto purchase(PurchaseRequestDto dtoRequest) {
		purchaseValidations.forEach(t -> t.valid(dtoRequest));
		
		Customer customer = repository.findByDocument(dtoRequest.document()).orElseThrow(
				() -> new CustomerNotFoundException("User not found for document: " + dtoRequest.document()));

		// Adiciona os produtos ao historico de compras do usuario
		addProductsToPurchaseHistory(dtoRequest, customer);

		// Persiste o histórico
		repository.save(customer);

		// montando a resposta
		return assembleResponse(dtoRequest, customer);
	}

	private void addProductsToPurchaseHistory(PurchaseRequestDto dtoRequest, Customer customer) {
		Instant instant = Instant.now();
		List<PurchaseRecord> purchaseRecords = dtoRequest.products().stream().map(p -> new PurchaseRecord(p.name(),
				p.price(), p.code(), instant, p.quantity(), Status.EM_ABERTO, customer)).collect(Collectors.toList());
		customer.getPurchaseRecord().addAll(purchaseRecords);
	}

	private PurchaseResponseDto assembleResponse(PurchaseRequestDto dtoRequest, Customer customer) {
		List<PurchasedProduct> purchasedProducts = dtoRequest.products().stream()
				.map(p -> new PurchasedProduct(p.name(), p.quantity(), p.price())).collect(Collectors.toList());

		BigDecimal total = purchasedProducts.stream().map(p -> p.value().multiply(new BigDecimal(p.quantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new PurchaseResponseDto(customer.getName(), purchasedProducts, total);
	}

	@Transactional
	public void undoPurchase(UndoPurchaseDto dtoRequest) {
		Customer customer = repository.findByPurchase(dtoRequest.purchaseId()).orElseThrow(
				() -> new CustomerNotFoundException("User not found for purchase id: " + dtoRequest.purchaseId()));
		
		  var list = customer.getPurchaseRecord();	
		  System.out.println("#####<------ Lista Antes ------->#####");
		  
		  for(PurchaseRecord x : list) {
			  System.out.println(x.getProductName());
		  }
		  		  

		    // Remover o elemento diretamente da lista original
		    list.removeIf(t -> t.getId().equals(dtoRequest.purchaseId()));
		  
		  System.out.println("#####<------ Lista Depois ------->#####");
		  for(PurchaseRecord x : list) {
			  System.out.println(x.getProductName());
		  }
		  
		  repository.save(customer);
		
	}



}
