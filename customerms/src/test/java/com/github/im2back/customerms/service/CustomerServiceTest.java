package com.github.im2back.customerms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.customerms.model.dto.datainput.PurchaseHistoryInDTO;
import com.github.im2back.customerms.model.dto.datainput.PurchasedProductsDTO;
import com.github.im2back.customerms.model.dto.datainput.RegisterCustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.CustomerDTO;
import com.github.im2back.customerms.model.dto.dataoutput.ProductDataToPdf;
import com.github.im2back.customerms.model.dto.dataoutput.PurchaseHistoryOutDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DailyTotalDTO;
import com.github.im2back.customerms.model.dto.dataoutput.metrics.DataForMetricsDTO;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.exeptions.CustomerNotFoundException;
import com.github.im2back.customerms.util.CustomerFactory;
import com.github.im2back.customerms.util.ExceptionFactory;
import com.github.im2back.customerms.util.UtilObjectsFactory;
import com.github.im2back.customerms.utils.PdfGenerator;
import com.github.im2back.customerms.validations.customervalidations.CustomerValidations;
import com.github.im2back.customerms.validations.exceptions.CustomerRegisterValidationException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
	
	@InjectMocks
	private CustomerService customerService;
	
	@Mock
	private CustomerRepository repository;
	
	@Spy
	private List<CustomerValidations> customerValidations = new ArrayList<>();
	
	@Mock 
	private CustomerValidations validation1;
	
	@Mock 
	private CustomerValidations validation2;
	
	@Mock
	private PdfGenerator pdfGenerator;
	
	@Captor
	private ArgumentCaptor<Customer> customerCaptor;


	@Test
	void findCustomerById_ShoudReturnCustomerDto_WhenIdIsValid() {
		//ARRANGE
			Long id = 1l;
			Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEM_ABERTO());
			BDDMockito.when(this.repository.findById(id)).thenReturn(customer);
			CustomerDTO responseExpected = CustomerFactory.createCustomerDTO();
		
		//ACT
			CustomerDTO response = this.customerService.findCustomerById(id);	
		
		//ASSERT
			verify(this.repository).findById(id);
			Assertions.assertEquals(responseExpected,response);
	}
	
	@Test
	void findCustomerById_ShoudThrowExceptionDto_WhenIdIsInvalid() {
		//ARRANGE
		Long id = 1l;
		doThrow(ExceptionFactory.createCustomerNotFoundException()).when(repository).findById(id);
	
	//ACT + ASSERT
		Assertions.assertThrows(CustomerNotFoundException.class, () -> this.customerService.findCustomerById(id));
		verify(this.repository).findById(id);
	}
	
	@Test
	void findCustomerWithUnpaidPurchases_ShouldReturnCustomerAndOpenPurchaseRecords_WhenDocumentIsValid() {
		//ARRANGE
			String document = "00769203213";
			Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEM_ABERTO());
			BDDMockito.when(this.repository.findByDocument(document)).thenReturn(customer);
			
		//ACT
			CustomerDTO response = this.customerService.findCustomerWithUnpaidPurchases(document);
		
		//ASSERT
			verify(this.repository).findByDocument(document);
			
			Assertions.assertEquals(1,response.purchaseRecord().size());
			
			response.purchaseRecord().forEach(t -> {
				Assertions.assertEquals(Status.EM_ABERTO, t.status());
			});		
	}
	
	@Test
	void saveNewCustomer_ShoudReturnCustomerDto_WhenIdIsValid() {
		//ARRANGE
			RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
			Customer customerReturn = CustomerFactory.createCustomerAndPurchaseEM_ABERTO();
			customerValidations.add(validation1);
			customerValidations.add(validation2);
						
			BDDMockito.when(this.repository.save(any())).thenReturn(customerReturn);
		
		//ACT
			CustomerDTO response = this.customerService.saveNewCustomer(registerCustomerDTO);
		
		//ASSERT
			verify(this.repository).save(customerCaptor.capture());
			Customer customerCaptured = customerCaptor.getValue();
			Assertions.assertEquals(registerCustomerDTO.document(),customerCaptured.getDocument());
			Assertions.assertEquals(registerCustomerDTO.name(),customerCaptured.getName());
			
			Assertions.assertEquals(response.document(),registerCustomerDTO.document());
			Assertions.assertEquals(response.name(),registerCustomerDTO.name());
			
			BDDMockito.then(validation1).should().valid(registerCustomerDTO);
			BDDMockito.then(validation2).should().valid(registerCustomerDTO);
	}
	
	@Test
	void saveNewCustomer_ShouldThrowCustomerRegisterValidationException_WhenBusinessValidationFails() {
		//ARRANGE
			RegisterCustomerDTO registerCustomerDTO = CustomerFactory.createRegisterCustomerDTO();
			customerValidations.add(validation1);
			List<String> messages = new ArrayList<>();
			messages.add("teste");
			doThrow(new CustomerRegisterValidationException(messages)).when(validation1).valid(registerCustomerDTO);
		
		//ACT + ASSERT
			Assertions.assertThrows(CustomerRegisterValidationException.class, () -> this.customerService.saveNewCustomer(registerCustomerDTO));
			BDDMockito.then(validation1).should().valid(registerCustomerDTO);

	}
	
	@Test
	void logicalCustomerDeletion_ShoudReturnPurchaseHistoryOutDTO_WhenDeletionIsSucess() {
		//ARRANGE
			String document = "00769203213";
			Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEM_ABERTO());
			BDDMockito.when(this.repository.findByDocument(document)).thenReturn(customer);
		
		//ACT
			 this.customerService.logicalCustomerDeletion(document);	
		
		//ASSERT
			verify(this.repository).findByDocument(document);
			
			verify(this.repository).save(customerCaptor.capture());
			Customer customerCaptured = customerCaptor.getValue();
			Assertions.assertEquals("00769203213", customerCaptured.getDocument());
			Assertions.assertEquals(false, customerCaptured.isActive(),"Deveria mudar o atributo para false");
	}
	
	@Test
	void registerPurchase_ShoudReturnPurchaseHistoryOutDTO_WhenSucess() {
		//ARRANGE
			String document = "00769203213";
			Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEmpity());
			BDDMockito.when(this.repository.findByDocument(document)).thenReturn(customer);
			BDDMockito.when(this.repository.save(any())).thenReturn(customer.get());			
			
			List<PurchasedProductsDTO> products = new ArrayList<>();
			products.add(new PurchasedProductsDTO("Nescau", new BigDecimal(19.99), "001", 2));
			PurchaseHistoryInDTO purchaseHistoryInDTO = new PurchaseHistoryInDTO(document, products);
		
		//ACT
			PurchaseHistoryOutDTO response = this.customerService.registerPurchase(purchaseHistoryInDTO);	
		
		//ASSERT
			verify(this.repository).findByDocument(document);
			
			verify(this.repository).save(customerCaptor.capture());
			Customer customerSaved = customerCaptor.getValue();
			
			Assertions.assertEquals("00769203213", customerSaved.getDocument());
			Assertions.assertEquals(customerSaved.getPurchaseRecord().size(), 1);
			Assertions.assertEquals(response.purchasedProducts().size(), 1);
			Assertions.assertEquals(response.purchasedProducts().get(0).value(), new BigDecimal(19.99));
			Assertions.assertEquals(response.purchasedProducts().get(0).productName(), "Nescau");
	}
	
	@Test
	void registerPurchaseAsync_ShoudReturnVoid_WhenDeletionIsSucess() {
		//ARRANGE
			String document = "00769203213";
			Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEmpity());
			BDDMockito.when(this.repository.findByDocument(document)).thenReturn(customer);
			BDDMockito.when(this.repository.save(any())).thenReturn(customer.get());			
			
			List<PurchasedProductsDTO> products = new ArrayList<>();
			products.add(new PurchasedProductsDTO("Nescau", new BigDecimal(19.99), "001", 2));
			PurchaseHistoryInDTO purchaseHistoryInDTO = new PurchaseHistoryInDTO(document, products);
		
		//ACT
			 this.customerService.registerPurchaseAsync(purchaseHistoryInDTO);	
		
		//ASSERT
			verify(this.repository).findByDocument(document);		
			verify(this.repository).save(customerCaptor.capture());
			Customer customerSaved = customerCaptor.getValue();
			
			Assertions.assertEquals("00769203213", customerSaved.getDocument());
			Assertions.assertEquals(customerSaved.getPurchaseRecord().size(), 1);
	}
	
	@Test
	void undoPurchase_ShoudReturnVoid_WhenSucess() {
		//ARRANGE
		Long id = 1l;	
		
		//ACT
			 this.customerService.undoPurchase(id);	
		
		//ASSERT
			verify(this.repository).undoPurchase(id);		
	}
	
	@Test
	void generatePurchaseInvoice_ShoudReturnVoid_WhenSucess() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//ARRANGE
		String document = "00769203213";
		Optional<Customer> customer = Optional.ofNullable(CustomerFactory.createCustomerAndPurchaseEM_ABERTO());
		BDDMockito.when(this.repository.findByDocument(document)).thenReturn(customer);
		
		
		Method getProductDataToPdfList =  CustomerService.class.getDeclaredMethod("getProductDataToPdfList", List.class);
		getProductDataToPdfList.setAccessible(true);
		Object retorno = getProductDataToPdfList.invoke(customerService, customer.get().getPurchaseRecord());
		List<ProductDataToPdf> productDataToPdfs = (List<ProductDataToPdf>) retorno;
	
		Method getPath =  CustomerService.class.getDeclaredMethod("getPath", String.class);
		getPath.setAccessible(true);
		Object retorno2 = getPath.invoke(customerService, customer.get().getName());
		String path = (String) retorno2;
		
		
		//ACT
			 this.customerService.generatePurchaseInvoice(document);	
		
		//ASSERT
			verify(this.pdfGenerator).generatePdf(productDataToPdfs, customer.get(), path);
	}
	
	@Test
	void clearDebt_ShoudReturnVoid_WhenSucess() {
		//ARRANGE
		String document = "00769203213";	
		
		//ACT
			 this.customerService.clearDebt(document);
		
		//ASSERT
			verify(this.repository).updateStatusByCustomerDocumentNative("PAGO", document, "EM_ABERTO");
	}
	
	@Test
	void individualPayment_ShoudReturnVoid_WhenSucess() {
		//ARRANGE
		Long id = 1l;	
		
		//ACT
			 this.customerService.individualPayment(id);
		
		//ASSERT
			verify(this.repository).individualPayment(Status.PAGO.toString(), id);
	}
	
	@Test
	void metrics_ShoudReturnDataForMetricsDTO_WhenSucess() {
		//ARRANGE	
		DataForMetricsDTO dataForMetricsDTO = UtilObjectsFactory.createDataForMetricsDTO(); 
		BDDMockito.when(this.repository.totalValueForLastMonth()).thenReturn(100.00);
		BDDMockito.when(this.repository.partialValueOfTheCurrentMonth()).thenReturn(70.00);
		BDDMockito.when(this.repository.partialVAlueForCurrentDay()).thenReturn(5.00);
		BDDMockito.when(this.repository.totalOutstandingAmount()).thenReturn(40.00);
		
		 List<Object[]> mockResults = new ArrayList<>();
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(1)), BigDecimal.valueOf(15.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(2)), BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(3)), BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(4)), BigDecimal.valueOf(13.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(5)), BigDecimal.valueOf(14.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(6)), BigDecimal.valueOf(16.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(7)), BigDecimal.valueOf(17.00).setScale(2, RoundingMode.HALF_UP)});
		
		BDDMockito.when(this.repository.findTotalValueForLast7DaysExcludingToday()).thenReturn(mockResults);
		
		//ACT
		DataForMetricsDTO response = this.customerService.metrics();	 
		
		//ASSERT
		Assertions.assertEquals(dataForMetricsDTO.totalValueForLastMonth(),response.totalValueForLastMonth());
		Assertions.assertEquals(dataForMetricsDTO.partialValueForCurrentMonth(),response.partialValueForCurrentMonth());
		Assertions.assertEquals(dataForMetricsDTO.partialValueForCurrentDay(),response.partialValueForCurrentDay());
		Assertions.assertEquals(dataForMetricsDTO.totalOutstandingAmount(),response.totalOutstandingAmount());
		
		Map<BigDecimal, DailyTotalDTO> dailyTotalMap = response.dataGraphicSevenDays()
				.stream()
				.collect(Collectors.toMap(DailyTotalDTO::totalValue, dto -> dto));		
	
		dataForMetricsDTO.dataGraphicSevenDays().forEach(t ->{
			DailyTotalDTO  dailyTotalDTO = dailyTotalMap.getOrDefault(t.totalValue(), null);
			Assertions.assertTrue(dailyTotalDTO != null);
		});
	}
	
	@Test
	void getTotalValuesForLast7Days_ShoudReturnListDailyTotalDTO_WhenSucess() {
		//ARRANGE
		
		 List<Object[]> mockResults = new ArrayList<>();
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(1)), BigDecimal.valueOf(15.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(2)), BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(3)), BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(4)), BigDecimal.valueOf(13.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(5)), BigDecimal.valueOf(14.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(6)), BigDecimal.valueOf(16.00).setScale(2, RoundingMode.HALF_UP)});
	        mockResults.add(new Object[]{Date.valueOf(LocalDate.now().minusDays(7)), BigDecimal.valueOf(17.00).setScale(2, RoundingMode.HALF_UP)});
		
		BDDMockito.when(this.repository.findTotalValueForLast7DaysExcludingToday()).thenReturn(mockResults);
		
		//ACT
		List<DailyTotalDTO> response = this.customerService.getTotalValuesForLast7Days();
		
		//ASSERT
		verify(this.repository).findTotalValueForLast7DaysExcludingToday();
		
		Map<BigDecimal, DailyTotalDTO> dailyTotalMap = response
				.stream()
				.collect(Collectors.toMap(DailyTotalDTO::totalValue, dto -> dto));		
	
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(15.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(13.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(14.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(16.00).setScale(2, RoundingMode.HALF_UP), null) != null);
			Assertions.assertTrue(dailyTotalMap.getOrDefault(BigDecimal.valueOf(17.00).setScale(2, RoundingMode.HALF_UP), null) != null);
	}
	
	
	
	
}
