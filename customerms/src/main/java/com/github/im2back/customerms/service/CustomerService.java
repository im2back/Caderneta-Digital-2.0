package com.github.im2back.customerms.service;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.im2back.customerms.model.dto.datainput.CustomerDto;
import com.github.im2back.customerms.model.dto.datainput.IndividualPaymentDto;
import com.github.im2back.customerms.model.dto.datainput.PurchaseRequestDto;
import com.github.im2back.customerms.model.dto.datainput.UndoPurchaseDto;
import com.github.im2back.customerms.model.dto.dataoutput.DailyTotal;
import com.github.im2back.customerms.model.dto.dataoutput.DataForMetricsDto;
import com.github.im2back.customerms.model.dto.dataoutput.GetCustomerDto;
import com.github.im2back.customerms.model.dto.dataoutput.ProductDataToPdf;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseResponseDto;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProduct;
import com.github.im2back.customerms.model.entities.customer.Address;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.utils.PdfGenerator;
import com.github.im2back.customerms.validations.customervalidations.CustomerValidations;
import com.github.im2back.customerms.validations.purchasevalidations.PurchaseValidations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repository;
	private final List<CustomerValidations> customerValidations;
	private final List<PurchaseValidations> purchaseValidations;
	private final PdfGenerator pdfGenerator;

	@Transactional(readOnly = true)
	private Customer findByCustomerPerDocument(String document) {
		return repository.findByDocument(document)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for document: " + document));
	}

	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerById(Long id) {
		Customer customer = repository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for id: " + id));
		return new GetCustomerDto(customer);
	}

	@Transactional(readOnly = true)
	public GetCustomerDto findCustomerByDocumentOrganizedPurchase(String document) {
		Customer customer = findByCustomerPerDocument(document);
		customer.getPurchaseRecord().removeIf(t -> t.getStatus() == Status.PAGO);
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
	public void logicalCustomerDeletion(String document) {
		Customer customer = findByCustomerPerDocument(document);
		customer.setActive(false);
		repository.save(customer);
	}

	@Transactional
	public PurchaseResponseDto purchase(PurchaseRequestDto dtoRequest) {
		Customer customer = findByCustomerPerDocument(dtoRequest.document());
		purchaseValidations.forEach(t -> t.valid(dtoRequest,customer));

		// Adiciona os produtos ao historico de compras do usuario
		addProductsToPurchaseHistory(dtoRequest, customer);
		
		
		// Persiste o histórico
		repository.save(customer);
		
		// montando a resposta
		return assembleResponse(dtoRequest, customer);
	}

	private void addProductsToPurchaseHistory(PurchaseRequestDto dtoRequest, Customer customer) {

		if (customer.getDocument().equals("7654321589")) {

			Instant instant = Instant.now();
			List<PurchaseRecord> purchaseRecords = dtoRequest.products().stream().map(p -> new PurchaseRecord(p.name(),
					p.price(), p.code(), instant, p.quantity(), Status.PAGO, customer)).collect(Collectors.toList());
			customer.getPurchaseRecord().addAll(purchaseRecords);
		}

		else {

			Instant instant = Instant.now();
			List<PurchaseRecord> purchaseRecords = dtoRequest.products().stream().map(p -> new PurchaseRecord(p.name(),
					p.price(), p.code(), instant, p.quantity(), Status.EM_ABERTO, customer))
					.collect(Collectors.toList());
			customer.getPurchaseRecord().addAll(purchaseRecords);
		}
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
		repository.undoPurchase(dtoRequest.purchaseId());
	}

	public void generatePurchaseInvoice(String document) {
		Customer customer = findByCustomerPerDocument(document);
		List<ProductDataToPdf> productDataToPdfList = getProductDataToPdfList(customer);

		pdfGenerator.generatePdf(productDataToPdfList, customer, getPath(customer));
		// new PdfGenerator(javaMailSender).generatePdf(productDataToPdfList, customer,
		// getPath(customer));
	}

	private List<ProductDataToPdf> getProductDataToPdfList(Customer customer) {
		List<PurchaseRecord> list1 = customer.getPurchaseRecord();
		list1.removeIf(t -> t.getStatus().equals(Status.PAGO));

		var ProductDataToPdfList = list1.stream().map(t -> new ProductDataToPdf(t)).collect(Collectors.toList());

		return ProductDataToPdfList;
	}

	private String getPath(Customer customer) {
		String userHome = System.getProperty("user.home");
		String desktopDirectory = Paths.get(userHome).toString();
		var path = Paths.get(desktopDirectory, customer.getName() + "_nota_fiscal_" + ".pdf").toString();
		return path;
	}

	@Transactional
	public void clearDebt(String document) {
	    repository.updateStatusByCustomerDocumentNative("PAGO", document, "EM_ABERTO");
	}
	
	
	public DataForMetricsDto metrics() {
		return new DataForMetricsDto(
				repository.totalValueForLastMonth(),
				repository.partialValueOfTheCurrentMonth(),
				repository.partialVAlueForCurrentDay(),
				repository.totalOutstandingAmount(),
				getTotalValuesForLast7Days());
	}

	@Transactional(readOnly = true)
	public List<DailyTotal> getTotalValuesForLast7Days() {
		List<Object[]> results = repository.findTotalValueForLast7DaysExcludingToday();
		List<DailyTotal> dailyTotals = new ArrayList<>();

		for (Object[] result : results) {
			Date sqlDate = (java.sql.Date) result[0];
			BigDecimal totalValue = (BigDecimal) result[1];
			LocalDate purchaseDate = sqlDate.toLocalDate();
			dailyTotals.add(new DailyTotal(purchaseDate, totalValue));
		}

		return dailyTotals;
	}
	
	@Transactional
	public void individualPayment(@Valid IndividualPaymentDto dtoRequest) {
		repository.individualPayment(Status.PAGO, dtoRequest.purchaseId());
	}
	

}
