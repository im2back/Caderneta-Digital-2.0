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

import com.github.im2back.customerms.model.dto.datainput.NewCustomerDTO;
import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.dataoutput.CustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.ProductDataToPdf;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseHistoryOutDTO;
import com.github.im2back.customerms.model.dto.dataoutput.PurchasedProductDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DailyTotalDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DataForMetricsDTO;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.PurchaseRecord;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.utils.PdfGenerator;
import com.github.im2back.customerms.validations.customervalidations.CustomerValidations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository repository;
	private final List<CustomerValidations> customerValidations;
	private final PdfGenerator pdfGenerator;

	@Transactional(readOnly = true)
	private Customer findCustomerByDocument(String document) {
		return repository.findByDocument(document)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for document: " + document));
	}

	@Transactional(readOnly = true)
	public CustomerDTO findCustomerById(Long id) {
		Customer customer = repository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("User not found for id: " + id));
		return new CustomerDTO(customer);
	}

	@Transactional(readOnly = true)
	public CustomerDTO findCustomerByDocumentOrganizedPurchase(String document) {
		Customer customer = findCustomerByDocument(document);
		customer.getPurchaseRecord().removeIf(t -> t.getStatus() == Status.PAGO);
		return new CustomerDTO(customer);
	}

	@Transactional
	public CustomerDTO saveNewCustomer(NewCustomerDTO dtoIn) {

		customerValidations.forEach(t -> t.valid(dtoIn));

		Customer customer = repository.save(new Customer(dtoIn));
		
		return new CustomerDTO(customer);
	}

	@Transactional
	public void logicalCustomerDeletion(String document) {
		Customer customer = findCustomerByDocument(document);
		customer.setActive(false);
		repository.save(customer);
	}

	@Transactional
	public PurchaseHistoryOutDTO purchase(PurchaseHistoryInDTO dtoRequest) {

		Customer customer = findCustomerByDocument(dtoRequest.document());
		addPurchaseToCustomerHistory(dtoRequest, customer);
		repository.save(customer);

		return assembleResponse(dtoRequest, customer);
	}

	private void addPurchaseToCustomerHistory(PurchaseHistoryInDTO dtoRequest, Customer customer) {
		Instant instant = Instant.now();
		Status status = Status.DEFINIR_STATUS.setStatus(customer.getDocument());
	
		List<PurchaseRecord> purchaseRecords = dtoRequest.products().stream()
					.map(dto -> new PurchaseRecord(dto, instant, customer, status)).collect(Collectors.toList());
		customer.getPurchaseRecord().addAll(purchaseRecords);
	}

	private PurchaseHistoryOutDTO assembleResponse(PurchaseHistoryInDTO dtoRequest, Customer customer) {
		List<PurchasedProductDTO> purchasedProducts = dtoRequest.products().stream()
				.map(p -> new PurchasedProductDTO(p.name(), p.quantity(), p.price())).collect(Collectors.toList());

		BigDecimal total = purchasedProducts.stream().map(p -> p.value().multiply(new BigDecimal(p.quantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new PurchaseHistoryOutDTO(customer.getName(), purchasedProducts, total);
	}

	@Transactional
	public void undoPurchase(Long id) {
		repository.undoPurchase(id);
	}

	public void generatePurchaseInvoice(String document) {
		Customer customer = findCustomerByDocument(document);
		List<ProductDataToPdf> productDataToPdfList = getProductDataToPdfList(customer);

		pdfGenerator.generatePdf(productDataToPdfList, customer, getPath(customer));
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

	public DataForMetricsDTO metrics() {
		return new DataForMetricsDTO(repository.totalValueForLastMonth(), repository.partialValueOfTheCurrentMonth(),
				repository.partialVAlueForCurrentDay(), repository.totalOutstandingAmount(),
				getTotalValuesForLast7Days());
	}

	@Transactional(readOnly = true)
	public List<DailyTotalDTO> getTotalValuesForLast7Days() {
		List<Object[]> results = repository.findTotalValueForLast7DaysExcludingToday();
		List<DailyTotalDTO> dailyTotals = new ArrayList<>();

		for (Object[] result : results) {
			Date sqlDate = (java.sql.Date) result[0];
			BigDecimal totalValue = (BigDecimal) result[1];
			LocalDate purchaseDate = sqlDate.toLocalDate();
			dailyTotals.add(new DailyTotalDTO(purchaseDate, totalValue));
		}

		return dailyTotals;
	}

	@Transactional
	public void individualPayment(Long purchaseId) {
		repository.individualPayment(Status.PAGO.toString(), purchaseId);
			
	}

}
