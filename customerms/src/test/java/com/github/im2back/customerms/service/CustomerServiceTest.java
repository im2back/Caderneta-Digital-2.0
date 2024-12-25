package com.github.im2back.customerms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.im2back.customerms.model.dto.dataoutput.DataForMetricsDto;
import com.github.im2back.customerms.model.dto.dataoutput.ProductDataToPdf;
import com.github.im2back.customerms.model.entities.customer.Customer;
import com.github.im2back.customerms.model.entities.purchase.Status;
import com.github.im2back.customerms.repositories.CustomerRepository;
import com.github.im2back.customerms.service.util.Util;
import com.github.im2back.customerms.utils.PdfGenerator;
import com.github.im2back.customerms.validations.customervalidations.CustomerValidations;
import com.github.im2back.customerms.validations.purchasevalidations.PurchaseValidations;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private CustomerRepository repository;


	private CustomerService customerService;

	@Captor
	private ArgumentCaptor<Customer> customerCaptor;

	@Spy
	private List<CustomerValidations> customerValidations = new ArrayList<>();

	@Mock
	private CustomerValidations validador1;

	@Mock
	private CustomerValidations validador2;
	
	@Spy
	private List<PurchaseValidations> purchaseValidations  = new ArrayList<>();

	@Mock
	private PurchaseValidations validador3;

	@Mock
	private PurchaseValidations validador4;
	
	@Mock
    private PdfGenerator pdfGenerator;
	
	@Captor
	private ArgumentCaptor<List<ProductDataToPdf>> listProductDataToPdfCaptor;
	
	@Captor
	private ArgumentCaptor<String> stringCaptor;
		
	@BeforeEach
	void setUp() {
	    purchaseValidations = new ArrayList<>();
	    purchaseValidations.add(validador3);
	    purchaseValidations.add(validador4);

	    customerService = new CustomerService(repository, customerValidations, purchaseValidations, pdfGenerator);
	}

	@Test
	@DisplayName("deveria retorna um usuário com base no id informado")
	void findCustomerById() {

		// ARRANGE
		Long id = 1l;
		BDDMockito.when(repository.findById(id)).thenReturn(Util.customerOptional);

		// ACT
		var response = customerService.findCustomerById(id);

		// ASSERT
		verify(repository, times(1)).findById(1l);

		assertEquals(1l, response.id(), "Deveria retornar um Dto com o mesmo id do parametro");

		assertEquals("123456789", response.document(),
				"Deveria retornar um Dto com o mesmo documento do customer retornado pelo banco");
	}

	@Test
	@DisplayName("Deveria retornar um client com  sua lista de compras triada")
	void findCustomerByDocumentOrganizedPurchase() {
		// ARRANGE
		String document = "123456789";
		BDDMockito.when(repository.findByDocument(document)).thenReturn(Util.customerOptional);

		// ACT
		var response = customerService.findCustomerByDocumentOrganizedPurchase(document);

		// ASSERT
		verify(repository, times(1)).findByDocument("123456789");
		Assertions.assertEquals("123456789", response.document(),
				"Deveria retornar um customer com documento igual ao do parametro");

		response.purchaseRecord().forEach(t -> Assertions.assertEquals(Status.EM_ABERTO, t.status(),
				"Deveria retornar apenas Status de valor EM,_ABERTO"));
	}

	@Test
	@DisplayName("Deveria  salvar um usuario no banco e retornar um dto")
	void saveNewCustomer() {

		// ARRANGE
		BDDMockito.when(repository.save(any(Customer.class))).thenReturn(Util.customer);
		customerValidations.add(validador1);
		customerValidations.add(validador2);
		
		// ACT
		var response = customerService.saveNewCustomer(Util.customerDto);

		// ASSERT
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		
		Assertions.assertEquals(Util.customerDto.document(), customerCaptured.getDocument(),
				"O usuário cadastrado deveria ter documento igual ao do parâmetro");
		
		Assertions.assertEquals(Util.customerDto.document(), response.document(),
				"O usuário retornado deveria ter documento igual ao do parâmetro");
		
		BDDMockito.then(validador1).should().valid(Util.customerDto);  
		BDDMockito.then(validador2).should().valid(Util.customerDto);  
	}
	
	@Test
	@DisplayName("Deveria fazer a exclusao logica apartir de um documento")
	void logicalCustomerDeletion() {

		// ARRANGE
		String document = "123456789";
		BDDMockito.when(repository.findByDocument(document)).thenReturn(Util.customerOptional);

		// ACT
		customerService.logicalCustomerDeletion(document);

		// ASSERT
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		Assertions.assertEquals(document, customerCaptured.getDocument(), 
				"O documento do cliente salvo deveria ser igual ao do parametro de entrada");
		
		Assertions.assertEquals(false, customerCaptured.isActive(),"O status deveria ser false");
		verify(repository,times(1)).findByDocument("123456789");
	}

	
	@Test
	@DisplayName("Deveria criar um registro de compra apartir do dto recebido")
	void purchase() {

		// ARRANGE
		BDDMockito.when(repository.findByDocument(Util.purchaseRequestDto.document()))
		.thenReturn(Util.customerListaVaziaOptional);
				
		// ACT
		var response = customerService.purchase(Util.purchaseRequestDto);

		// ASSERT
		verify(repository,times(1)).findByDocument(Util.purchaseRequestDto.document());
		
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		Assertions.assertEquals(Util.purchaseRequestDto.document(), customerCaptured.getDocument(),
				"o documento do cliente salvo deve ser igual ao do parametro");
		
		BDDMockito.then(validador3).should().valid(Util.purchaseRequestDto);
		BDDMockito.then(validador4).should().valid(Util.purchaseRequestDto);
		
		Assertions.assertEquals(new BigDecimal(1249.85).setScale(2, RoundingMode.HALF_UP), response.total().setScale(2, RoundingMode.HALF_UP), 
				"O valor total deveria ser a soma total de cada produto x quantidade");
		
		Assertions.assertEquals("Jefferson", response.customerName(),"O nome do cliente deve ser igual ao retornado pelo banco");
		
		//testando indiretamente se o metodo addProductsToPurchaseHistory foi chamado
		Set<String> expectedProductNames = new HashSet<>(Arrays.asList("Produto A", "Produto B"));

		customerCaptured.getPurchaseRecord().forEach(t -> {
		    Assertions.assertTrue(expectedProductNames.contains(t.getProductName()));
		});
		
	}
	
	
	@Test
	@DisplayName("Deveria remover uma compra do historico")
	void undoPurchase() {

		// ARRANGE
		BDDMockito.when(repository.findByPurchase(Util.undoPurchaseDto.purchaseId())).thenReturn(Util.customerOptional);
			
		// ACT
		customerService.undoPurchase(Util.undoPurchaseDto);
		
		// ASSERT
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		
		 Assertions.assertFalse(customerCaptured.getPurchaseRecord().contains(Util.purchaseRecord1),
				 "Não deveria conter o objeto a ser removido");
		 Assertions.assertEquals(Util.customer.getDocument(), customerCaptured.getDocument(),
				 "O usuario salvo deve ser o mesmo que o retornado pelo bando de dados");
	}
	
	
	@Test
	@DisplayName("Deveria gerar uma nota da conta do cliente, apartir de um documento informado ")
	void generatePurchaseInvoice() {

		// ARRANGE
		String document = "123456789";
		BDDMockito.when(repository.findByDocument(document)).thenReturn(Util.customerOptional2);
		
		// ACT
		customerService.generatePurchaseInvoice(document);

		// ASSERT
		verify(repository,times(1)).findByDocument(document);
		BDDMockito.then(pdfGenerator).should().generatePdf(listProductDataToPdfCaptor.capture()
				, customerCaptor.capture(), stringCaptor.capture());
		
		Assertions.assertEquals(1l,customerCaptor.getValue().getId(),
				" O id do cliente passado para o método generatePdf deve ser igual ao retornado pelo banco");
		
		Assertions.assertEquals(2, customerCaptor.getValue().getPurchaseRecord().size());
		
		Assertions.assertTrue(customerCaptor.getValue().getPurchaseRecord().contains(Util.purchaseRecord4),
				"Deveria conter os objetos com status EM_ABERTO");
		
		Assertions.assertTrue(customerCaptor.getValue().getPurchaseRecord().contains(Util.purchaseRecord5),
				"Deveria conter os objetos com status EM_ABERTO");
	}
	
	
	@Test
	@DisplayName("Deveria mudar para status PAGO todas as comprar de um cliente ")
	void clearDebt() {

		// ARRANGE
		String document = "123456789";
		BDDMockito.when(repository.findByDocument(document)).thenReturn(Util.customerOptional);
		
		// ACT
		customerService.clearDebt(document);

		// ASSERT
		verify(repository,times(1)).findByDocument(document);
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		Assertions.assertEquals(1l, customerCaptured.getId(),"O id do usuario salvo deve corresponde ao retornado pelo banco");
		Assertions.assertEquals(document, customerCaptured.getDocument(),"O document do usuario salvo deve corresponde ao retornado pelo banco");
		customerCaptured.getPurchaseRecord().forEach(t -> 
		Assertions.assertEquals(Status.PAGO, t.getStatus(),"todos os status devem ser PAGO"));

	}
	
	@Test
	@DisplayName("Deveria consultar o repositorio e retornar os dados das métricas")
	void DataForMetricsDto() {

		// ARRANGE

		// ACT
		var response = customerService.metrics();
		
		// ASSERT
		verify(repository,times(1)).totalValueForLastMonth();
		verify(repository,times(1)).partialValueOfTheCurrentMonth();
		verify(repository,times(1)).partialVAlueForCurrentDay();
		verify(repository,times(1)).totalOutstandingAmount();
		verify(repository,times(1)).findTotalValueForLast7DaysExcludingToday();
		Assertions.assertEquals(DataForMetricsDto.class, response.getClass(),
				"Deveria retornar uma classe do tipo DataForMetricsDto ");
	}
	

	@Test
	@DisplayName("Deveria desfazer uma unica compra apartir de um  UndoPurchaseDto ")
	void test() {

		// ARRANGE
		BDDMockito.when(repository.findByPurchase(Util.undoPurchaseDto.purchaseId())).thenReturn(Util.customerOptional3);
		
		// ACT
		customerService.individualPayment(Util.undoPurchaseDto);

		// ASSERT
		BDDMockito.then(repository).should().save(customerCaptor.capture());
		Customer customerCaptured = customerCaptor.getValue();
		Assertions.assertEquals(3, customerCaptured.getPurchaseRecord().size());
		
		Assertions.assertEquals(1l, customerCaptured.getId(),
				"O id do customer salvo deve ser igual ao id do customer retornado pelo banco");
				
		customerCaptured.getPurchaseRecord().forEach(t -> {
			if(t.getId().equals(1)) {
				Assertions.assertTrue(t.getStatus() == Status.PAGO);
			}
		});

	}
	

}
