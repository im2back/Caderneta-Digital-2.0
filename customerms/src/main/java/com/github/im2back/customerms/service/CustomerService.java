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
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProduct;
import com.github.im2back.customerms.model.entities.customer.Address;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerById(Long id) {
		Customer customer = repository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for id: " + id));
		return new GetCustomerDto(customer);
	}

	@Transactional
	public GetCustomerDto saveNewCustomer(CustomerDto c) {
		Customer customer = repository.save(new Customer(c.name(), c.document(), c.email(), c.phone(),
				new Address(c.address().streetName(), c.address().houseNumber(), c.address().complement())));
		return new GetCustomerDto(customer);
	}

	@Transactional
	public void deleteCustomerById(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public PurchaseResponseDto purchase(PurchaseRequestDto dtoRequest) {
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
	            .map(p -> new PurchasedProduct(p.name(), p.quantity(), p.price()))
	            .collect(Collectors.toList());

	    BigDecimal total = purchasedProducts.stream()
	            .map(p -> p.value().multiply(new BigDecimal(p.quantity())))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    return new PurchaseResponseDto(customer.getName(), purchasedProducts, total);
	}

}
